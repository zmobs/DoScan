package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.reedsolomon.RSEncoder;
import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;

import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 加密信息组件
 */
public class GateKeeper {

    /******************  单例实现区域  *********************/
    private static volatile GateKeeper instance;

    private GateKeeper() {

    }

    public static GateKeeper getInstance() {

        if (instance == null) {
            synchronized (GateKeeper.class) {
                if (instance == null) {
                    instance = new GateKeeper();
                }
            }
        }
        return instance;
    }
    /******************  end 单例实现区域  *********************/

    /************************  建造者实现模式  ************************/
    /**
     * 密钥
     */
    private int key;
    /**
     * 原始数据
     */
    private byte[] sourceInfo;
    /**
     * 加密单元开始标识符
     */
    private final byte startFlag = 69;
    /**
     * 加密单元结束标识符
     */
    private final byte endFlag = -110;
    /**
     * 最终得机密数据
     */
    private byte[] finalInfo;
    /**
     * 原始数据最大字节数
     */
    private final int MAX_SOURCE_LENGTH = 1024;
    /**
     * 每个加密单元得长度
     */
    private final int perEncryptUnitLen = 258;
    /**
     * 单个加密单元的长度限制,因为我们使用的有限域参数限制
     */
    private final int perUnitLen = 128;
    /**
     * 待修改的数字数量
     */
    public int willModifyNum;


    /**
     * 指定待加密的原始数据
     * @param content 原始字符串
     * @return gk引用链
     */
    public GateKeeper source(String content) {

        // 判断source
        if (StringUtil.isEmpty(content)) {
            Log.bomb("不能加密空字符内容");
        }
        int length = content.length();
        if (length > MAX_SOURCE_LENGTH) {
            Log.bomb("过多的加密内容");
        }

        this.sourceInfo = content.getBytes();

        return this;
    }


    /**
     * 指定加密信息
     * @param finalInfoStr
     * @return
     */
    public GateKeeper messageInfo(String finalInfoStr) {
        this.finalInfo = Base64.getDecoder().decode(finalInfoStr);
        return this;
    }

    /**
     * 指定加解密的key
     * @param keyVal
     * @return
     */
    public GateKeeper key(int keyVal) {
        this.key = keyVal;
        return this;
    }

    /**
     * 执行最小加密单位的加密操作
     * @param src 原始数据
     * @return 加密后的数据
     */
    private byte[] doUnitEncrypt(byte[] src) {

        // 先获得RS编码部分
        int sourceLength = src.length;
        int[] rsCode = RSEncoder.getInstance().getRSCode(src, sourceLength);
        int rsLength = rsCode.length;

        // 将rs byte 转换为int
        byte[] rsBytes = new byte[rsLength];
        for (int i = 0; i < rsLength; i++) {
            rsBytes[i] = (byte) (rsCode[i]);
        }

        // 将两个数组穿插安防
        if (sourceLength != rsLength) {
            Log.bomb("算法出错，两端数据不一致");
        }

        // 最终加密后的消息长度
        int messageLength = sourceLength * 2 + 2;
        byte[] messageBytes = new byte[messageLength];

        int maxModifyNum = sourceLength / 2;
        willModifyNum = ThreadLocalRandom.current().nextInt(maxModifyNum);

        // 对原始编码和RS编码进行拼接操作，得到最终的消息码字
        for (int i = 1; i <= sourceLength; i++) {
            int firstByteIndex = 2 * i - 1;
            int secondByteIndex = 2 * i;

            messageBytes[firstByteIndex] = src[i - 1];
            messageBytes[secondByteIndex] = rsBytes[i - 1];
            // 用配置的key进行按位异或操作
            messageBytes[firstByteIndex] ^= key;
            messageBytes[secondByteIndex] ^= key;
        }

        // 随机遍历一次，进行随机错码
        for (int i = 0; i < willModifyNum; i++) {
            int byteIndex = ThreadLocalRandom.current().nextInt(messageLength);
            int byteValue = ThreadLocalRandom.current().nextInt(255);
            // FixBug 因为首位数字不一定是单字节编码，所以免疫范围扩大到双字节
            if(byteIndex < 2 || byteIndex > 253){
                // 如果是开始和最后一个字节不进行加密。
                continue;
            }
            messageBytes[byteIndex] = (byte) byteValue;
        }

        // 给每个加密单元的 头尾标识位置 进行处理
        messageBytes[0] = startFlag;
        messageBytes[messageLength - 1] = endFlag;

        return messageBytes;
    }


    /**
     * 加密原始信息
     * @return
     */
    public String encryptInfo() {

        int sourceLength = sourceInfo.length;
        int encryptTime = sourceLength / perUnitLen;
        int leftNum = sourceLength % perUnitLen;

        // 如果存在不满的 加密单元，那么计算出最终加密后单元长度
        int leftCodeNum = leftNum;
        if (leftNum > 0) {
            leftCodeNum = leftNum * 2 + 2;
        }
        int messageSize = encryptTime * (perUnitLen * 2 + 2) + (leftCodeNum);
        byte[] messageByte = new byte[messageSize];

        // 计数位，最终加密结果的偏移量
        int offset = 0;
        // 源码的偏移量
        int sourceOffset = 0;

        for (int i = 0; i <= encryptTime; i++) {
            byte[] tmpByte;
            if (i == encryptTime) {
                if (leftNum == 0) {
                    continue;
                }
                tmpByte = new byte[leftNum];
                System.arraycopy(sourceInfo, sourceOffset, tmpByte, 0, leftNum);
                sourceOffset += leftNum;
            } else {
                tmpByte = new byte[perUnitLen];
                System.arraycopy(sourceInfo, sourceOffset, tmpByte, 0, perUnitLen);
                sourceOffset += perUnitLen;
            }
            byte[] tempMessage = doUnitEncrypt(tmpByte);
            int tempMessageLen = tempMessage.length;
            System.arraycopy(tempMessage, 0, messageByte, offset, tempMessageLen);
            offset += tempMessageLen;
        }

        String message = new String(Base64.getEncoder().encode(messageByte));
        return message;

    }


    /**
     * 进行单位解密操作
     * @param info 待解密信息
     * @return
     */
    private byte[] doUnitDecrypt(byte[] info) {

        int finalLength = info.length;
        byte firstByte = info[0];
        byte lastByte = info[finalLength - 1];
        // 如果每个解密单元不符合约定，则认为他们是不对的
        if (firstByte != startFlag || lastByte != endFlag || (finalLength % 2 != 0)) {
            Log.bomb("wrong rule");
        }

        int sourceLength = finalLength / 2 - 1;
        byte[] sourceBytes = new byte[sourceLength];
        byte[] rsBytes = new byte[sourceLength];
        for (int i = 1; i <= sourceLength; i++) {
            sourceBytes[i - 1] = info[2 * i - 1];
            rsBytes[i - 1] = info[2 * i];
            // 用配置的key进行按位异或操作
            sourceBytes[i - 1] ^= key;
            rsBytes[i - 1] ^= key;
        }

        byte[] rightOrderBytes = new byte[finalLength - 2];
        System.arraycopy(sourceBytes, 0, rightOrderBytes, 0, sourceLength);
        System.arraycopy(rsBytes, 0, rightOrderBytes, sourceLength, sourceLength);

        int[] sourceInt;
        try {
            sourceInt = RSEncoder.getInstance().decodeRSCode(rightOrderBytes, sourceLength);
        } catch (Exception e) {
            return null;
        }
        byte[] sourceStrBytes = new byte[sourceLength];
        for (int i = 0; i < sourceLength; i++) {
            sourceStrBytes[i] = (byte) (sourceInt[i]);
        }

        return sourceStrBytes;
    }



    /**
     * 解密原始数据
     *
     * @return
     */
    public String decryptInfo() {

        // 计算得到 最终消息长度 和 原始信息长度
        int finalInfoLength = finalInfo.length;
        int unitNum = finalInfoLength / perEncryptUnitLen;
        int leftNum = 0;
        int finalInfoLeft = finalInfoLength %  perEncryptUnitLen;
        if(finalInfoLength %  perEncryptUnitLen > 0){
            leftNum = (finalInfoLeft - 2) / 2;
        }
        int srcInfoLen = unitNum * perUnitLen + leftNum;

        byte[] srcInfo = new byte[srcInfoLen];

        int offset = 0;
        int sourceOffset = 0;
        int leftByteNum = finalInfoLength;

        while (leftByteNum > 0) {

            byte[] sourceUnit;
            byte[] msgByte;
            int passByteNum;

            if (leftByteNum < perEncryptUnitLen) {
                if (leftByteNum == 0) {
                    continue;
                }
                passByteNum = leftByteNum;
            } else {
                passByteNum = perEncryptUnitLen;
            }

            sourceUnit = new byte[passByteNum];
            System.arraycopy(finalInfo, offset, sourceUnit, 0, passByteNum);
            msgByte = doUnitDecrypt(sourceUnit);
            int msgLen = msgByte.length;
            System.arraycopy(msgByte,0,srcInfo,sourceOffset,msgLen);


            sourceOffset +=msgLen;
            offset += passByteNum;
            leftByteNum = finalInfoLength - offset;

        }

        String encodeStr;
        encodeStr = new String(srcInfo);

        return encodeStr;

    }


}

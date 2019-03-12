package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.reedsolomon.RSEncoder;
import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class GateKeeper {

    /******************  单例实现区域  *********************/

    private static volatile GateKeeper instance;
    private final int MAX_SOURCE_LENGTH = 1024;

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
    private int key;
    private byte[] sourceInfo;
    private final byte startFlag = 69;
    private final byte endFlag = -110;
    private byte[] finalInfo;

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


    public GateKeeper finalInfo(String finalInfoStr) {
        this.finalInfo = Base64.getDecoder().decode(finalInfoStr);
        return this;
    }

    public GateKeeper key(int keyVal) {
        this.key = keyVal;
        return this;
    }

    public int willModifyNum;

    private byte[] doUnitEncrypt(byte[] src) {

        // 后面拼接
        int sourceLength = src.length;
        int[] rsInts = RSEncoder.getInstance().getRSCode(src, sourceLength);
        int rsLength = rsInts.length;

        // 将rs byte 转换为int
        byte[] rsBytes = new byte[rsLength];
        for (int i = 0; i < rsLength; i++) {
            rsBytes[i] = (byte) (rsInts[i]);
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

        for (int i = 1; i <= sourceLength; i++) {

            int firstByteIndex = 2 * i - 1;
            int secondByteIndex = 2 * i;

            messageBytes[firstByteIndex] = src[i - 1];
            messageBytes[secondByteIndex] = rsBytes[i - 1];
            // 用配置的key进行按位异或操作
            messageBytes[firstByteIndex] ^= key;
            messageBytes[secondByteIndex] ^= key;

        }

        // 随机遍历一次
        for (int i = 0; i < willModifyNum; i++) {
            int byteIndex = ThreadLocalRandom.current().nextInt(messageLength);
            int byteValue = ThreadLocalRandom.current().nextInt(256);
            messageBytes[byteIndex] = (byte) byteValue;
        }

        // 给每个加密单元的 头尾标识位置 进行处理
        messageBytes[0] = startFlag;
        messageBytes[messageLength - 1] = endFlag;

        return messageBytes;
    }

    // 单个加密单元的长度限制
    final int perUnitLen = 128;

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

    final int perEncryptUnitLen = 258;

    /**
     * 解密原始数据
     *
     * @return
     */
    public String decryptInfo() {

        // 首先对finalinfo base64之后的数据进行解密
        int finalInfoLength = finalInfo.length;
        int srcInfoLen = finalInfoLength - finalInfoLength / perEncryptUnitLen - (finalInfoLength %  perEncryptUnitLen > 0 ? 1 : 0);
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

package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.reedsolomon.RSEncoder;
import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private String sourceStr;
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


//        try {
//            this.sourceInfo = content.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            Log.bomb("不支持字符集合");
//        }
        try {
            this.sourceInfo = Base64.getEncoder().encode(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.bomb("不支持字符集合");
        }
        this.sourceStr = content;
        return this;
    }


    public GateKeeper finalInfo(byte[] finalInfo) {
        this.finalInfo = finalInfo;
        return this;
    }

    public GateKeeper key(int keyVal) {
        this.key = keyVal;
        return this;
    }

    private byte[] doUnitEncrypt(byte[] src){

        // 后面拼接
        int sourceLength = src.length;
        int[] rsInts = RSEncoder.getInstance().getRSCode(src, sourceLength);

        int rsLength = rsInts.length;

        byte[] rsBytes = new byte[rsLength];
        for (int i = 0; i < rsLength; i++) {
            rsBytes[i] = (byte) (rsInts[i]);
        }

        // 将两个数组穿插安防
        if (sourceLength != rsLength) {
            Log.bomb("算法出错，两端数据不一致");
        }

        int messageLength = sourceLength * 2 + 2;
        byte[] messageBytes = new byte[messageLength];

        messageBytes[0] = startFlag;

        int maxModifyNum = sourceLength / 2;
        int willModifyNum = ThreadLocalRandom.current().nextInt(maxModifyNum);

        for (int i = 1; i <= sourceLength; i++) {

            int firstByteIndex = 2 * i - 1;
            int secondByteIndex = 2 * i;

            messageBytes[firstByteIndex] = sourceInfo[i - 1];
            messageBytes[secondByteIndex] = rsBytes[i - 1];
            // 用配置的key进行按位异或操作
            messageBytes[firstByteIndex] ^= key;
            messageBytes[secondByteIndex] ^= key;

        }

        // 随机遍历一次
//        for(int i = 0; i < willModifyNum;i++){
//            int byteIndex = ThreadLocalRandom.current().nextInt(messageLength);
//            int byteValue = ThreadLocalRandom.current().nextInt(256);
//            messageBytes[byteIndex] = (byte) byteValue;
//        }

        // 随机修改2位数字
        messageBytes[messageLength - 1] = endFlag;
//        messageBytes = Base64.getEncoder().encode(messageBytes);
//        messageBytes = Base64.getEncoder().encode(messageBytes);

        return messageBytes;
    }


    public byte[] encryptInfo() {
        final int perUnitLen = 128;

        int sourceLength = sourceInfo.length;
        int encryptTime = sourceLength / perUnitLen;
        int leftNum = sourceLength % perUnitLen;
        int leftCodeNum = leftNum;
        if(leftNum > 0){
            leftCodeNum = leftNum * 2 + 2;
        }
        Log.d("sourceLength  --- " + sourceLength);
        int messageSize = encryptTime * (perUnitLen * 2 + 2) + (leftCodeNum);
        Log.d("messageSize  --- " + messageSize);
        byte[] messgaeByte = new byte[messageSize];
        int offset = 0;
        int sourceOffset = 0;
        for(int i = 0; i <= encryptTime;i++){

            byte[] tmpByte;
            if(i == encryptTime){
                if(leftNum == 0){
                    continue;
                }
                tmpByte = new byte[leftNum];

                System.arraycopy(sourceInfo,sourceOffset,tmpByte,0,leftNum);
                sourceOffset += leftNum;
            }else{
                tmpByte = new byte[perUnitLen];
                System.arraycopy(sourceInfo,sourceOffset,tmpByte,0,perUnitLen);
                sourceOffset += perUnitLen;
            }

            byte[] tempMessage = doUnitEncrypt(tmpByte);

            Log.d("offset  --- " + offset);
            Log.d("tempMessage  --- " + Arrays.toString(tempMessage));

            int tempMessageLen = tempMessage.length;

            System.arraycopy(tempMessage,0,messgaeByte,offset,tempMessageLen);
            offset += tempMessageLen;
        }


        return messgaeByte;


    }


    private String doUnitDecrypt(byte[] info){

        int finalLength = info.length;
        byte firstByte = info[0];
        byte lastByte = info[finalLength - 1];
        if (firstByte != startFlag || lastByte != endFlag || (finalLength % 2 != 0)) {
            return "";
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
            return "";
        }
        byte[] sourceStrBytes = new byte[sourceLength];
        for (int i = 0; i < sourceLength; i++) {
            sourceStrBytes[i] = (byte) (sourceInt[i]);
        }

        try {
            String finalStr = new String(sourceStrBytes, "UTF-8");
            return finalStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 解密原始数据
     *
     * @return
     */
    public String decryptInfo() {
        // 首先对finalinfo base64之后的数据进行解密


        final int perUnitLen = 258;
        int finalInfoLength = finalInfo.length;


        StringBuilder stringBuilder = new StringBuilder();
        int offset = 0;
        int leftByteNum = finalInfoLength - offset;

        while(leftByteNum > 0){

            byte[] sourceUnit;
            String messageStr;
            leftByteNum = finalInfoLength - offset;


            if(leftByteNum < perUnitLen){

                if(leftByteNum == 0){
                    continue;
                }
                sourceUnit = new byte[leftByteNum];
                System.arraycopy(finalInfo,offset,sourceUnit,0,leftByteNum);
                offset += leftByteNum;
                messageStr = doUnitDecrypt(sourceUnit);
            }else{
                sourceUnit = new byte[perUnitLen];

                System.arraycopy(finalInfo,offset,sourceUnit,0,perUnitLen);
                Log.d("sourceUnit   --- " + Arrays.toString(sourceUnit));
                offset += perUnitLen;
                messageStr = doUnitDecrypt(sourceUnit);
            }
            stringBuilder.append(messageStr);
        }

        String encodeStr = null;
        try {
            encodeStr = new String(Base64.getDecoder().decode(stringBuilder.toString().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;

    }


}

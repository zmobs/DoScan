package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.reedsolomon.RSEncoder;
import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;

import java.io.UnsupportedEncodingException;
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
        try {
            this.sourceInfo = content.getBytes("UTF-8");
            Log.d("sourceInfo   ---  " + sourceInfo.length);
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

    public byte[] encryptInfo() {

        // 后面拼接
        int sourceLength = sourceInfo.length;
        int[] rsInts = RSEncoder.getInstance().getRSCode(sourceInfo, sourceLength);

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
        int modifyedNum = 0;

//        int denominatorNum =

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
        for(int i = 0; i < willModifyNum;i++){
            int byteIndex = ThreadLocalRandom.current().nextInt(messageLength);
            int byteValue = ThreadLocalRandom.current().nextInt(256);
            messageBytes[byteIndex] = (byte) byteValue;
            modifyedNum += 1;
        }

        Log.d("maxModifyNum  ---  " + maxModifyNum);
        Log.d("modifyedNum  ---  " + modifyedNum);

        // 随机修改2位数字
        messageBytes[messageLength - 1] = endFlag;
        messageBytes = Base64.getEncoder().encode(messageBytes);
        return messageBytes;

    }

    /**
     * 解密原始数据
     *
     * @return
     */
    public String decryptInfo() {
        // 首先对finalinfo base64之后的数据进行解密
        finalInfo = Base64.getDecoder().decode(finalInfo);

        int finalLength = finalInfo.length;
        byte firstByte = finalInfo[0];
        byte lastByte = finalInfo[finalLength - 1];
        if (firstByte != startFlag || lastByte != endFlag || (finalLength % 2 != 0)) {
            //Log.huming("非法消息");
            return "";
        }
        int sourceLength = finalLength / 2 - 1;
        byte[] sourceBytes = new byte[sourceLength];
        byte[] rsBytes = new byte[sourceLength];
        for (int i = 1; i <= sourceLength; i++) {
            sourceBytes[i - 1] = finalInfo[2 * i - 1];
            rsBytes[i - 1] = finalInfo[2 * i];
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


}

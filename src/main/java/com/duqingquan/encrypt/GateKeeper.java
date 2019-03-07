package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.reedsolomon.RSEncoder;
import com.duqingquan.doscan.qrcode.util.Log;

import java.util.Arrays;

public class GateKeeper {

    /******************  单例实现区域  *********************/

    private static volatile GateKeeper instance;

    private GateKeeper(){

    }

    public static GateKeeper getInstance(){

        if(instance == null){
            synchronized (GateKeeper.class){
                if(instance == null){
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
    public GateKeeper source(String content){
        this.sourceInfo = content.getBytes();
        this.sourceStr = content;
        return this;
    }


    public GateKeeper finalInfo(byte[] finalInfo){
        this.finalInfo = finalInfo;
        return this;
    }

    public GateKeeper key(int keyVal){
        this.key = keyVal;
        return this;
    }

    public byte[] encryptInfo(){

        // 后面拼接
        int sourceLength = sourceInfo.length;
        int[] rsInts = RSEncoder.getInstance().getRSCode(sourceInfo,sourceLength);
        int rsLength = rsInts.length;

        byte[] rsBytes = new byte[rsLength];
        for(int i = 0; i <rsLength;i++){
            rsBytes[i] = (byte) (rsInts[i] & 0xFF);
        }
        // 将两个数组穿插安防
        if(sourceLength != rsLength){
            Log.bomb("算法出错，两端数据不一致");
        }

        int messageLength = sourceLength * 2 + 2;
        byte[] messageBytes = new byte[messageLength];

        Log.d("sourceInfo  ---  " + Arrays.toString(sourceInfo));
        Log.d("rsBytes  ---  " + Arrays.toString(rsBytes));

        messageBytes[0] = startFlag;
        for(int i = 1; i <= sourceLength;i++){
            messageBytes[2 * i - 1] = sourceInfo[i - 1];
            messageBytes[2 * i] = rsBytes[i - 1];
            // 用配置的key进行按位异或操作
            messageBytes[2 * i - 1] ^= key;
            messageBytes[2 * i] ^= key;
        }
        // 随机修改2位数字 todo
        messageBytes[messageLength - 1] = endFlag;

        Log.d("messageBytes  ---  " + Arrays.toString(messageBytes));

        return messageBytes;

    }

    /**
     * 解密原始数据
     * @return
     */
    public String decryptInfo(){

        int finalLength = finalInfo.length;
        byte firstByte = finalInfo[0];
        byte lastByte = finalInfo[finalLength - 1];
        if(firstByte != startFlag || lastByte != endFlag || (finalLength % 2 != 0)){
            Log.bomb("非法消息");
        }
        int sourceLength = finalLength / 2 - 1;
        byte[] sourceBytes = new byte[sourceLength];
        byte[] rsBytes = new byte[sourceLength];
        Log.d("finalInfo  ---  " + Arrays.toString(finalInfo));
        for(int i = 1; i <= sourceLength;i++){
            sourceBytes[i - 1] = finalInfo[2 * i - 1];
            rsBytes[i - 1] = finalInfo[2 * i];
            // 用配置的key进行按位异或操作
            sourceBytes[i - 1] ^= key;
            rsBytes[i - 1] ^= key;
        }

        byte[] rightOrderBytes = new byte[finalLength - 2];
        Log.d("sourceBytes  ---  " + Arrays.toString(sourceBytes));
        System.arraycopy(sourceBytes,0,rightOrderBytes,0,sourceLength);
        Log.d("rsBytes  ---  " + Arrays.toString(rsBytes));
        System.arraycopy(rsBytes,0,rightOrderBytes,sourceLength,sourceLength);
        Log.d("rightOrderBytes  ---  " + Arrays.toString(rightOrderBytes));
        int[] sourceInt = RSEncoder.getInstance().decodeRSCode(rightOrderBytes,sourceLength);

        byte[] sourceStrBytes = new byte[sourceLength];
        Log.d("sourceLength  ----  " + sourceLength);
        for(int i = 0; i < sourceLength; i++){
            sourceStrBytes[i] = (byte) (sourceInt[i] & 0xFF);
        }


        return new String(sourceStrBytes);

    }



}

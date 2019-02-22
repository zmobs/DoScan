package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;
import com.google.zxing.ChecksumException;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;

import java.util.Arrays;

public class Demo {

    public static void main(String args[]){
        Log.d("加解密演示 start...");

        AbsEncryptEncoder encryptEncoder = new BCHEncoder();
        encryptEncoder = encryptEncoder.source("这是一条原始信息333".getBytes())
                .key(336);

//        String finalInfo = encryptEncoder.encrypt().string();

        byte[] finalInfo = encryptEncoder.encrypt().finalInfo;
        int dataLen = finalInfo.length;
        byte[] data = new byte[dataLen];
        System.arraycopy(finalInfo,0,data,0,dataLen);
        Log.d("finalInfo ..." + Arrays.toString(finalInfo));
        finalInfo[2] = 10;
        Log.d("finalInfo222 ..." + Arrays.toString(finalInfo));
//        String srcInfo  = encryptEncoder.decrypt().string();

        try {
            new BCHEncoder().correctErrors(data,data.length / 2);
        } catch (ChecksumException e) {
            e.printStackTrace();
        }
        Log.d("data ..." + Arrays.toString(data));
    }
}

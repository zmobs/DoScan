package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;
import com.google.zxing.ChecksumException;

import java.util.Arrays;

public class Demo {

    public static void main(String args[]){
        Log.d("加解密演示 start...");

//        AbsEncryptEncoder encryptEncoder = new BCHEncoder();
//
//
//        byte[] finalInfo = encryptEncoder.encrypt().finalInfo;
//
//        Log.d("finalInfo ..." + Arrays.toString(finalInfo));
//        finalInfo[2] = 10;
//        finalInfo[3] = 10;
//        Log.d("finalInfo222 ..." + Arrays.toString(finalInfo));
//
//        String strInfo  = encryptEncoder.decrypt().string();
//        Log.d("decryptInfo ..." + strInfo);


        byte[] enctryptMessage = GateKeeper.getInstance()
                .source("这是一条原始信息333")
                .key(336)
                .encryptInfo();
        String encryptMessage = new String(enctryptMessage);
        Log.d("encryptMessage ---  " + encryptMessage);


        String sourceInfo = GateKeeper.getInstance()
                .finalInfo(enctryptMessage)
                .key(336)
                .decryptInfo();
        Log.d("sourceInfo ---  " + sourceInfo);

    }
}

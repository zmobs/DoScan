package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;

public class Demo {

    public static void main(String args[]){
        Log.d("加解密演示 start...");

        ABSEncryptEncoder encryptEncoder = new BCHEncoder();
        encryptEncoder = encryptEncoder.source("这是一条原始信息333".getBytes())
                .key(336)
                .encrypt();
        String finalInfo = encryptEncoder.string();
        byte[] finalByte = encryptEncoder.finalBytes();


        Log.d("finalInfo ..." + finalInfo);
    }
}

package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;


public class Demo {

    public static void main(String args[]){
        Log.d("加解密演示 start...");



        byte[] enctryptMessage = GateKeeper.getInstance()
                .source("85ACF0SDF0F0AA")
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

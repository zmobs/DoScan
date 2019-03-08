package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;

import java.util.Base64;


public class Demo {

    public static void main(String args[]){
        Log.d("加解密演示 start...");

        byte[] enctryptMessage = GateKeeper.getInstance()
                .source("明年年初，中美合拍的西游记即将正式开机，我继续扮演美猴王孙悟空，" +
                        "我会用美猴王艺术形象努力创造一个正能量的形象，文体两开花，弘扬中华文化，希望大家能多多关注" )
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

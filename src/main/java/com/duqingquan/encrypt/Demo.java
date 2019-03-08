package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;



public class Demo {

    public static void main(String args[]){
        Log.d("加解密演示 start...");

        byte[] enctryptMessage = GateKeeper.getInstance()
                .source("明年年初，中美合拍的西游记即将正式开机，我继续扮演美猴王孙悟空。文体两开花文体两开花")
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

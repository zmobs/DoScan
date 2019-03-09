package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;



public class Demo {

    public static void main(String args[]){
        Log.d("加解密演示 start...");

        byte[] enctryptMessage = GateKeeper.getInstance()
                .source("小草偷偷地从土地里钻出来，嫩嫩的，绿绿的。园子里，田野里，瞧去，一大片一大片满是的。坐着，躺着，打两个滚，踢几脚球，赛几趟跑，捉几回迷藏。" )
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

package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;


public class Demo {

    public static void main(String args[]) {
        Log.d("加解密演示 start...");
        int errNum = 0;
        for (int i = 0; i < 1; i++) {
            byte[] enctryptMessage = GateKeeper.getInstance()
                    .source("小草偷偷地从土地里钻出来，嫩嫩的，绿绿的。园子里，")
                    .key(336)
                    .encryptInfo();


            String sourceInfo = GateKeeper.getInstance()
                    .finalInfo(enctryptMessage)
                    .key(336)
                    .decryptInfo();
//            Log.d("sourceInfo ---  " + sourceInfo);
            if (StringUtil.isEmpty(sourceInfo)) {
                errNum += 1;
            }
        }

        Log.d("errNum ---  " + errNum);


    }


}

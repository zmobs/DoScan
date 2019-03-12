package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;

import java.util.Base64;


public class Demo {

    public static void main(String args[]) {
        Log.d("加解密演示 start...");
        int errNum = 0;


        for (int i = 0; i < 1; i++) {

            String enctryptMessage = GateKeeper.getInstance()
                    .source("小草偷偷地从土里钻出来，嫩嫩的，绿绿的。园子里，田野里，瞧去，一大片一大片满是的。" +
                            "坐着，躺着，打两个滚，踢几脚球，赛几趟跑，捉几回迷藏。风轻悄悄的，草软绵绵的。")
                    .key(336)
                    .encryptInfo();
            Log.d("enctryptMessage ---  " + enctryptMessage);

            String sourceInfo = GateKeeper.getInstance()
                    .finalInfo(enctryptMessage)
                    .key(336)
                    .decryptInfo();
            Log.d("sourceInfo ---  " + sourceInfo);

            if (StringUtil.isEmpty(sourceInfo)) {
                errNum += 1;
                Log.d("willModifyNum ---  " + GateKeeper.getInstance().willModifyNum);
            }

        }

        Log.d("errNum ---  " + errNum);


    }


}

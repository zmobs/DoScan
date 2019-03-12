package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;

import java.util.Base64;


public class Demo {

    public static void main(String args[]) {
        Log.d("加解密演示 start...");
        int errNum = 0;


        for (int i = 0; i < 1000; i++) {

            String enctryptMessage = GateKeeper.getInstance()
                    .source("盼望着,盼望着,东风来了,春天的脚步近了。 一切都像刚睡醒的样子,欣欣然张开了眼。山朗润起来了,水涨起来了,太阳的脸红起来了。" +
                            "小草偷偷地从土里钻出来，嫩嫩的，绿绿的。园子里，田野里，瞧去，一大片一大片满是的。" +
                            "坐着，躺着，打两个滚，踢几脚球，赛几趟跑，捉几回迷藏。风轻悄悄的，草软绵绵的。")
                    .key(336)
                    .encryptInfo();

            String sourceInfo = GateKeeper.getInstance()
                    .finalInfo(enctryptMessage)
                    .key(336)
                    .decryptInfo();

            if (StringUtil.isEmpty(sourceInfo)) {
                errNum += 1;
            }

        }

        Log.d("errNum ---  " + errNum);


    }


}

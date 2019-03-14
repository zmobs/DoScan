package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;


public class Demo {

    public static void main(String args[]) {
        Log.d("加解密演示 start...");
        String sourceContent = "盼望着，盼望着，东风来了，春天的脚步近了。" +
                "一切都像刚睡醒的样子，欣欣然张开了眼。山朗润起来了，水涨起来了，太阳的脸红起来了。";

        int errNum = 0;
        for (int i = 0; i < 10000; i++) {

            String encryptMessage = GateKeeper.getInstance()
                    .source(sourceContent)
                    .key(336)
                    .encryptInfo();

            String sourceInfo = GateKeeper.getInstance()
                    .messageInfo(encryptMessage)
                    .key(336)
                    .decryptInfo();

            if (!sourceInfo.equals(sourceContent)) {
                errNum += 1;
            }
        }

        Log.d("errNum  -- " + errNum);


    }


}

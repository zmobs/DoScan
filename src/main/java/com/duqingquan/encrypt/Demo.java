package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;


public class Demo {

    public static void main(String args[]) {
        Log.d("加解密演示 start...");
        String sourceContent = "dJLdCJiVnDvM9JUpsom9";
        int errNum = 0;

        for (int i = 0; i < 10; i++) {

            String encryptMessage = GateKeeper.getInstance()
                    .source(sourceContent)
                    .key(119)
                    .encryptInfo();
            Log.d("\"" + encryptMessage+"\",");
            String sourceInfo = GateKeeper.getInstance()
                    .messageInfo(encryptMessage)
                    .key(119)
                    .decryptInfo();
            if (!sourceInfo.equals(sourceContent)) {
                errNum += 1;
            }
        }

        Log.d("errNum  -- " + errNum);


    }


}

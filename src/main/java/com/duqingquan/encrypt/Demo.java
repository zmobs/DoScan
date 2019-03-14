package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;

import java.util.Base64;


public class Demo {

    public static void main(String args[]) {
        Log.d("加解密演示 start...");
        String sourceContent = "AAAAbbb";

        int errNum = 0;
//        for(int i = 0; i < 1000; i++){
            String encryptMessage = GateKeeper.getInstance()
                    .source(sourceContent)
                    .key(336)
                    .encryptInfo();
            Log.d("encryptMessage   ---   " + encryptMessage);
            String sourceInfo = GateKeeper.getInstance()
                    .finalInfo(encryptMessage)
                    .key(336)
                    .decryptInfo();
            Log.d("sourceInfo   ---" + sourceInfo);
            Log.d("sourceContent   ---" + sourceContent);
            if(!sourceInfo.equals(sourceContent)){
                errNum += 1;
            }
//        }

        Log.d("errNum  -- " + errNum);


    }


}

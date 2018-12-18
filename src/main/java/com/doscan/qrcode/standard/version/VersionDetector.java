package com.doscan.qrcode.standard.version;

import com.doscan.qrcode.util.LogUtil;

public class VersionDetector {

public VersionDetector(){

}
        public void test(){
            for(int i = 1; i < 41; i++){
                Version version = new Version(i);
                LogUtil.log("version ---- " + i + "   " + version.getRemainder());
            }
        }

}

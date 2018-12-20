package com.doscan.qrcode.standard.version;

import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.standard.qrcode.InputThing;
import com.doscan.qrcode.util.Log;

public class VersionDetector {

    EncodeStrategy encodeStrategy;

    public VersionDetector(EncodeStrategy strategy) {
        this.encodeStrategy = strategy;
    }


    public Version detectVersion(InputThing inputThing){

        if(encodeStrategy == EncodeStrategy.HIGN_QUALITY){

        }else if(encodeStrategy == EncodeStrategy.MIN_SIZE){

        }
        return new Version(1);
    }


    public static boolean checkSpecVersion(Version version, InputThing inputThing){
        if(version == null || inputThing == null){
            Log.bomb("参数不能为空");
        }
        int headBitLen = inputThing.getModeIndicator().getSize();
        int charCount = version.getCharIndicatorCount(inputThing);

        return false;
//        inputThing.
    }


}

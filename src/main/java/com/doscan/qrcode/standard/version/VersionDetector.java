package com.doscan.qrcode.standard.version;

import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.doscan.qrcode.standard.qrcode.input.InputThing;
import com.doscan.qrcode.standard.table.VersionECTable;
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


    /**
     * 检测指定版本是否可以使用
     * @param version
     * @param correctLevel
     * @param inputThing
     * @return
     */
    public static boolean checkSpecVersion(Version version,
                                           ErrorCorrectLevel correctLevel,
                                           InputThing inputThing){
        if(version == null || inputThing == null){
            Log.bomb("参数不能为空");
        }
        int headBitLen = inputThing.getModeIndicator().getSize();
        int charCountBitLen = version.getCharIndicatorCount(inputThing);
        int dataLen = inputThing.getBits().getSize();
        // 需要的bit数量
        int needBit = headBitLen + charCountBitLen + dataLen;
        Log.d("needBit  --  " + needBit);
        //  TODO 需要实现检测指定版本，且纠错级别的情况下容量校验
        VersionECTable.instance.
        return false;
    }


}

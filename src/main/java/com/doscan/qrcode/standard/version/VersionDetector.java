package com.doscan.qrcode.standard.version;

import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.doscan.qrcode.standard.qrcode.errorcorrect.ECBlock;
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

        int inputLen = inputThing.getStrContent().length();

        VersionECTable.ECBlockInfo ecBlockInfo= VersionECTable
                                            .instance
                                            .findBlockInfo(version.getVersionNumber(),correctLevel);

        int capacityCodes = ecBlockInfo.getCapacityCodeword();
        int capBits = capacityCodes * 8;
        int dataCapBit = capBits - headBitLen - charCountBitLen;
        int capForType = inputThing.getCapNum(dataCapBit);
        if(inputLen > capForType){
            return false;
        }else{
            return true;
        }

    }


}

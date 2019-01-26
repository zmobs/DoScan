package com.doscan.qrcode.standard.version;

import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.doscan.qrcode.standard.qrcode.errorcorrect.ECBlock;
import com.doscan.qrcode.standard.qrcode.input.InputThing;
import com.doscan.qrcode.standard.table.VersionECTable;
import com.doscan.qrcode.util.Log;

import java.util.ArrayList;
import java.util.List;

public class VersionDetector {

    EncodeStrategy encodeStrategy;

    public VersionDetector(EncodeStrategy strategy) {
        this.encodeStrategy = strategy;
    }


    public VersionCap detectVersion(InputThing inputThing){

        if(encodeStrategy == EncodeStrategy.HIGN_QUALITY){
            for(int i = 1;i < 41;i++){
                Version perVersion = new Version(i);
                boolean isCap = checkSpecVersion(perVersion,ErrorCorrectLevel.H,inputThing);
                if(isCap){
                    return new VersionCap(perVersion,ErrorCorrectLevel.H);
                }
            }
            Log.bomb("too big for the high data");
        }else if(encodeStrategy == EncodeStrategy.MIN_SIZE){
            List<ErrorCorrectLevel> errorCorrectLevels = new ArrayList<>();
            errorCorrectLevels.add(ErrorCorrectLevel.H);
            errorCorrectLevels.add(ErrorCorrectLevel.Q);
            errorCorrectLevels.add(ErrorCorrectLevel.M);
            errorCorrectLevels.add(ErrorCorrectLevel.L);

            for(int i = 1;i < 41;i++){
                Version perVersion = new Version(i);
                for(ErrorCorrectLevel level : errorCorrectLevels){
                    boolean isCap = checkSpecVersion(perVersion,level,inputThing);
                    if(isCap){
                        return new VersionCap(perVersion,level);
                    }
                }
            }
            Log.bomb("too big for the normal data");
        }
        return null;
    }


    public static class VersionCap{

        Version version;
        ErrorCorrectLevel correctLevel;

        public VersionCap(Version version, ErrorCorrectLevel correctLevel) {
            this.version = version;
            this.correctLevel = correctLevel;
        }

        public Version getVersion() {
            return version;
        }

        public void setVersion(Version version) {
            this.version = version;
        }

        public ErrorCorrectLevel getCorrectLevel() {
            return correctLevel;
        }

        public void setCorrectLevel(ErrorCorrectLevel correctLevel) {
            this.correctLevel = correctLevel;
        }
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

        int inputLen = inputThing.getBits().getSize();

        VersionECTable.ECBlockInfo ecBlockInfo= VersionECTable
                                            .instance
                                            .findBlockInfo(version.getVersionNumber(),correctLevel);

        int capacityCodes = ecBlockInfo.getCapacityCodeword();
        int capBits = capacityCodes * 8;
        int dataCapBit = capBits - headBitLen - charCountBitLen;

        if(inputLen > dataCapBit){
            return false;
        }else{
            return true;
        }

    }


}

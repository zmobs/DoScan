package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.qrcode.input.InputThing;
import com.doscan.qrcode.standard.table.VersionECTable;
import com.doscan.qrcode.standard.version.VersionDetector;
import com.doscan.qrcode.util.HexUtil;
import com.doscan.qrcode.util.Log;

public class InputBitCaper {

    final String paddingCodeOne = "11101100";
    final String paddingCodeTwo = "00010001";


    public BitArray getInputBits(VersionDetector.VersionCap versionCap, InputThing inputThing){
        BitArray bitArray = new BitArray();
        bitArray.appendBitArray(inputThing.getModeIndicator());
        bitArray.appendBitArray(inputThing.getCountLength(versionCap.getVersion()));
        bitArray.appendBitArray(inputThing.getBits());

        VersionECTable.ECBlockInfo ecBlockInfo = VersionECTable.instance
                .findBlockInfo(versionCap.getVersion().getVersionNumber(),versionCap.getCorrectLevel());
        int maxBitNum = ecBlockInfo.getCapacityCodeword() * 8;
        int terminatorNum = maxBitNum - bitArray.getSize();
        final int maxTerminal = 4;
        if(terminatorNum > maxTerminal){
            terminatorNum = maxTerminal;
        }
        bitArray.appendBits(0,terminatorNum);
        int nowBitNum = bitArray.getSize();
        int leftNum = nowBitNum % 8;
        // 待添加数字
        int appendNum = 0;
        if(leftNum != 0){
            appendNum = 8 - leftNum;
        }
        bitArray.appendBits(0,appendNum);

        int paddingBitNum = maxBitNum - bitArray.getSize();
        int paddingByteNum = paddingBitNum / 8;
        for(int i = 0; i < paddingByteNum;i++){
            if(i % 2 == 0){
                bitArray.appendBitArray(HexUtil.strToBitArray(paddingCodeOne));
            }else{
                bitArray.appendBitArray(HexUtil.strToBitArray(paddingCodeTwo));
            }
        }
        return bitArray;
    }

}

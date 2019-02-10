package com.duqingquan.doscan.qrcode.standard.qrcode;

import com.duqingquan.doscan.qrcode.proto.BitArray;
import com.duqingquan.doscan.qrcode.standard.qrcode.input.InputThing;
import com.duqingquan.doscan.qrcode.standard.table.VersionECTable;
import com.duqingquan.doscan.qrcode.standard.version.VersionDetector;
import com.duqingquan.doscan.qrcode.util.HexUtil;
import com.duqingquan.doscan.qrcode.util.Log;

public class InputBitCaper {

    final String paddingCodeOne = "11101100";
    final String paddingCodeTwo = "00010001";



    public BitArray getInputBits(VersionDetector.VersionCap versionCap, InputThing inputThing){

        BitArray bitArray = new BitArray();
        bitArray.appendBitArray(inputThing.getModeIndicator());
        Log.defaultLog().record("模式头部标识比特序列：  " + inputThing.getModeIndicator());
        bitArray.appendBitArray(inputThing.getCountLength(versionCap.getVersion()));
        Log.defaultLog().record("长度标识比特序列：  " + inputThing.getModeIndicator());
        bitArray.appendBitArray(inputThing.getBits());
        Log.defaultLog().record("内容比特序列：  " + inputThing.getModeIndicator());


        VersionECTable.ECBlockInfo ecBlockInfo = VersionECTable.instance
                .findBlockInfo(versionCap.getVersion().getVersionNumber(),
                                versionCap.getCorrectLevel());
        Log.defaultLog().record("查找对应的纠错码块信息：  " + ecBlockInfo.getEcbNum());

        int maxBitNum = ecBlockInfo.getCapacityCodeword() * 8;
        int terminatorNum = maxBitNum - bitArray.getSize();
        final int maxTerminal = 4;
        if(terminatorNum > maxTerminal){
            terminatorNum = maxTerminal;
        }
        Log.defaultLog().record("填充纠错码块比特：  " + terminatorNum);
        bitArray.appendBits(0,terminatorNum);
        int nowBitNum = bitArray.getSize();
        int leftNum = nowBitNum % 8;
        // 待添加数字
        int appendNum = 0;
        if(leftNum != 0){
            appendNum = 8 - leftNum;
        }
        bitArray.appendBits(0,appendNum);
        Log.defaultLog().record("对齐8个比特，添加空白比特个数：  " + appendNum);
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

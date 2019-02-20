package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.reedsolomon.GFPoly;
import com.duqingquan.doscan.qrcode.reedsolomon.RSEncoder;
import com.duqingquan.doscan.qrcode.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class BCHEncoder extends AbsEncryptEncoder {

    private int offset = 0;

    @Override
    public AbsEncryptEncoder key(int key) {
        super.key(key);
        // offset = key div 2
        this.offset = key >> 1;
        return this;
    }

    @Override
    public byte[] doEncrypt() {
        int srcLen = srcInfo.length;
        int subSize = srcLen + 1;
        byte[] finalByte = new byte[subSize];
        // 环境检测通过，才会执行到这里
        for(int i = 0; i < srcLen;i++){
            byte perByte = srcInfo[i];
            perByte ^= key;
            finalByte[i] = perByte;
        }
        finalByte[srcLen] = EncryptedFlag;
        // 标准加密动作完成，进行BCH纠错码计算
        /**************************************/
        // 纠错码字节不存在负数，需要换证
        // 得到了生成多项式
        int[] ecInts = RSEncoder.getInstance().getRSCode(finalByte,subSize);


        byte[] bytes = new byte[subSize * 2];
        for(int i = 0; i < subSize;i++){
            bytes[i] = finalByte[i];
        }
        for(int i = 0; i < ecInts.length;i++){
            bytes[i + subSize] = (byte) ecInts[i];
        }

        Log.d("finalByte  ----  " + Arrays.toString(finalByte));
        Log.d("ecInts  ----  " + Arrays.toString(ecInts));
        Log.d("bytes  ----  " + Arrays.toString(bytes));
        return bytes;
    }

    @Override
    public byte[] doDecrypt() {
        int srcLen = finalInfo.length;
        byte flag = finalInfo[srcLen - 1];
        if(flag != EncryptedFlag){
            Log.bomb("尚未加密的内容，请检查");
        }
        int srcPrimeLength = srcLen - 1;
        srcInfo = new byte[srcPrimeLength];

        // 环境检测通过，才会执行到这里
        for(int i = 0; i < srcPrimeLength;i++){
            byte perByte = finalInfo[i];
            perByte ^= key;
            srcInfo[i] = perByte;
        }
        // 标准加密动作完成，进行BCH纠错码计算
        return srcInfo;
    }
}

package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;

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
        byte[] finalByte = new byte[srcLen + 1];
        // 环境检测通过，才会执行到这里
        for(int i = 0; i < srcLen;i++){
            byte perByte = srcInfo[i];
            perByte ^= key;
            finalByte[i] = perByte;
        }
        finalByte[srcLen] = EncryptedFlag;
        // 标准加密动作完成，进行BCH纠错码计算
        return finalByte;
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

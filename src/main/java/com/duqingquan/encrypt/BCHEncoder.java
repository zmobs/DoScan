package com.duqingquan.encrypt;

public class BCHEncoder extends ABSEncryptEncoder {

    private int offset = 0;

    @Override
    public ABSEncryptEncoder key(int key) {
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
}

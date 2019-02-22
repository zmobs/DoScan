package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.reedsolomon.GFPoly;
import com.duqingquan.doscan.qrcode.reedsolomon.RSEncoder;
import com.duqingquan.doscan.qrcode.util.Log;
import com.google.zxing.ChecksumException;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;

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
//        Log.d("finalByte 1111 ----  " + Arrays.toString(finalByte));

        // 纠错码字节不存在负数，需要换证
        // 得到了生成多项式
        int[] ecInts = RSEncoder.getInstance().getRSCode(finalByte,subSize);

//        Log.d("ecInts  ----  " + Arrays.toString(ecInts));
        byte[] bytes = new byte[subSize * 2];
        for(int i = 0; i < subSize;i++){
            bytes[i] = finalByte[i];
        }
        for(int i = 0; i < ecInts.length;i++){
            bytes[i + subSize] = (byte) ecInts[i];
        }
//        Log.d("bytes  ----  " + Arrays.toString(bytes));
        finalInfo = bytes;
        return bytes;
    }


    public void correctErrors(byte[] codewordBytes, int numDataCodewords) throws ChecksumException {

        ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(GenericGF.QR_CODE_FIELD_256);

        int numCodewords = codewordBytes.length;
        // First read into an array of ints
        int[] codewordsInts = new int[numCodewords];
        for (int i = 0; i < numCodewords; i++) {
            codewordsInts[i] = codewordBytes[i] & 0xFF;
        }
        try {
            rsDecoder.decode(codewordsInts, codewordBytes.length - numDataCodewords);
        } catch (ReedSolomonException ignored) {
            throw ChecksumException.getChecksumInstance();
        }
        // Copy back into array of bytes -- only need to worry about the bytes that were data
        // We don't care about errors in the error-correction codewords
        for (int i = 0; i < numDataCodewords; i++) {
            codewordBytes[i] = (byte) codewordsInts[i];
        }
    }

    @Override
    public byte[] doDecrypt() {
        int subSize = finalInfo.length / 2;
        int[] ecInts = RSEncoder.getInstance().decodeRSCode(finalInfo,subSize);

        for (int i = 0; i < subSize; i++) {
            finalInfo[i] = (byte) ecInts[i];
        }
        Log.d("finalInfo ..." + Arrays.toString(finalInfo));

        srcInfo = new byte[subSize - 1];

        // 环境检测通过，才会执行到这里
        for(int i = 0; i < subSize - 1;i++){
            byte perByte = finalInfo[i];
            perByte ^= key;
            srcInfo[i] = perByte;
        }
        // 标准加密动作完成，进行BCH纠错码计算
        return srcInfo;
    }
}

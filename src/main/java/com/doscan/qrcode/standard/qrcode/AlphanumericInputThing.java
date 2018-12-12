package com.doscan.qrcode.standard.qrcode;

/**
 * 英文数字混合模式
 */
public class AlphanumericInputThing extends InputThing{

    @Override
    public boolean isMatch(String content) {
        return false;
    }

    @Override
    public int[] getBits() {
        return new int[0];
    }
}

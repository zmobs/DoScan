package com.doscan.qrcode.standard.qrcode;

/**
 * 双字节日本字文本模式
 */
public class ShiftJISInputThing extends InputThing{
    @Override
    public boolean isMatch(String content) {
        return false;
    }

    @Override
    public int[] getBits() {
        return new int[0];
    }
}

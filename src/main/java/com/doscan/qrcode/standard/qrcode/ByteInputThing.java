package com.doscan.qrcode.standard.qrcode;

/**
 * 字节模式
 */
public class ByteInputThing extends InputThing{

    @Override
    public boolean isMatch(String content) {
        return false;
    }

    @Override
    public int[] getBits() {
        return new int[0];
    }
}

package com.doscan.qrcode.standard.qrcode;

/**
 * 字节模式
 */
public class MixInputThing extends InputThing{
    @Override
    protected boolean isMatch() {
        return false;
    }

    @Override
    protected int[] getBits() {
        return new int[0];
    }
}

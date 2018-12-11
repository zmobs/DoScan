package com.doscan.qrcode.standard.qrcode;

public class AlphanumericInputThing extends InputThing{
    @Override
    protected boolean isMatch() {
        return false;
    }

    @Override
    protected int[] getBits() {
        return new int[0];
    }
}

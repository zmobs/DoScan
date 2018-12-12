package com.doscan.qrcode.standard.qrcode;

/**
 * 英文数字混合模式
 */
public class AlphanumericInputThing extends InputThing{

    @Override
    public Byte[] getModeIndicator() {
        Byte[] indicator =  {0,0,1,0};
        return indicator;
    }

    @Override
    public boolean isMatch(String content) {

        return false;
    }

    @Override
    public Byte[] getBits() {
        return new Byte[0];
    }

    @Override
    public String getName() {
        return "英文数字混合模式";
    }
}

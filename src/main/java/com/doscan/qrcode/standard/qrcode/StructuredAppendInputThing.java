package com.doscan.qrcode.standard.qrcode;

/**
 * 字节模式
 */
public class StructuredAppendInputThing extends InputThing{


    @Override
    public Byte[] getModeIndicator() {
        Byte[] indicator = {0,0,1,1};
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
        return "混合输入模式";
    }


}

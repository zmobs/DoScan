package com.doscan.qrcode.standard.qrcode;

/**
 * 字节模式
 */
public class ByteInputThing extends InputThing{

    @Override
    public Byte[] getModeIndicator() {
        Byte[] indicator = {0,1,0,0};
        return indicator;
    }

    @Override
    public boolean isMatch(String content) {
        return false;
    }

    @Override
    public Byte[] getBits(String content) {
        return new Byte[0];
    }


    @Override
    public String getName() {
        return "字节模式";
    }
}

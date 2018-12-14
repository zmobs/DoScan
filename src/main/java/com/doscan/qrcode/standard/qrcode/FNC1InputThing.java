package com.doscan.qrcode.standard.qrcode;

/**
 * FNC1 模式
 */
public class FNC1InputThing extends InputThing{


    @Override
    public Byte[] getModeIndicator() {
        Byte[] indicator = {0,0,0,0};
        // 1st 0101
        // 2st 1001
        // terminator 0000
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
        return "混合输入模式";
    }


}

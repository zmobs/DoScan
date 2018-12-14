package com.doscan.qrcode.standard.qrcode;

/**
 * 输入的事物
 */
public abstract class InputThing {


    public abstract Byte[] getModeIndicator();

    public abstract boolean isMatch(String content);

    public abstract Byte[] getBits(String content);

    public abstract String getName();

}

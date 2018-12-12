package com.doscan.qrcode.standard.qrcode;

/**
 * 输入的事物
 */
public abstract class InputThing {


    public abstract boolean isMatch(String content);

    public abstract int[] getBits();

}

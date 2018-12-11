package com.doscan.qrcode.standard.qrcode;

/**
 * 输入的事物
 */
public abstract class InputThing {


    protected abstract boolean isMatch();

    protected abstract int[] getBits();

}

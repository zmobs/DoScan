package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.proto.BitArray;

/**
 * 输入的事物
 */
public abstract class InputThing {


    public abstract BitArray getModeIndicator();

    public abstract boolean isMatch(String content);

    public abstract BitArray getBits(String content);

    public abstract String getName();

}

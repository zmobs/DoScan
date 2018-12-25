package com.doscan.qrcode.standard.qrcode.input;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.version.Version;

/**
 * 字节模式
 */
public class ByteInputThing extends InputThing {

    @Override
    public BitArray getModeIndicator() {
        BitArray indicator = new BitArray();
        indicator.appendBit(false);
        indicator.appendBit(true);
        indicator.appendBit(false);
        indicator.appendBit(false);
        return indicator;
    }

    @Override
    public boolean isMatch() {
        return false;
    }

    @Override
    public BitArray getBits() {
        return new BitArray();
    }


    @Override
    public String getName() {
        return "字节模式";
    }

    @Override
    public BitArray getCountLength(Version version) {
        return null;
    }
}

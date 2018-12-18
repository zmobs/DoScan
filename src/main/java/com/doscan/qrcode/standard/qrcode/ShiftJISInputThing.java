package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.util.HexUtil;

/**
 * 双字节日本字文本模式
 */
public class ShiftJISInputThing extends InputThing{

    @Override
    public BitArray getModeIndicator() {
        String indicator = "1000";
        return HexUtil.strToBitArray(indicator);
    }

    @Override
    public boolean isMatch(String content) {
        return false;
    }

    @Override
    public BitArray getBits(String content) {
        return new BitArray();
    }

    @Override
    public String getName() {
        return "shift jis编码模式";
    }
}

package com.doscan.qrcode.standard.qrcode.input;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.version.Version;
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
    public int getCapNum(int codewordNum) {
        return 0;
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
        return "shift jis编码模式";
    }

    @Override
    public BitArray getCountLength(Version version) {
        return null;
    }
}
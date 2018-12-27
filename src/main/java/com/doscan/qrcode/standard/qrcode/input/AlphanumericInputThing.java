package com.doscan.qrcode.standard.qrcode.input;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.version.Version;

import java.util.regex.Pattern;

/**
 * 英文数字混合模式
 */
public class AlphanumericInputThing extends InputThing {

    @Override
    public BitArray getModeIndicator() {
        BitArray indicator = new BitArray();
        indicator.appendBit(false);
        indicator.appendBit(false);
        indicator.appendBit(true);
        indicator.appendBit(false);
        return indicator;
    }

    @Override
    public int getCapNum(int codewordNum) {
        return 0;
    }

    @Override
    public boolean isMatch() {
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z\\+\\-\\*\\$\\%\\.\\/\\:])*$");
        boolean result =  pattern
                .matcher(content)
                .matches();
        return result;
    }

    @Override
    public BitArray getBits() {
        return new BitArray();
    }


    @Override
    public String getName() {
        return "英文数字混合模式";
    }

    @Override
    public BitArray getCountLength(Version version) {
        return null;
    }
}

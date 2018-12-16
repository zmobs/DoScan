package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.proto.BitArray;

import java.util.regex.Pattern;

/**
 * 英文数字混合模式
 */
public class AlphanumericInputThing extends InputThing{

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
    public boolean isMatch(String content) {
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z\\+\\-\\*\\$\\%\\.\\/\\:])*$");
        boolean result =  pattern
                .matcher(content)
                .matches();
        return result;
    }

    @Override
    public BitArray getBits(String content) {
        return new BitArray();
    }


    @Override
    public String getName() {
        return "英文数字混合模式";
    }
}

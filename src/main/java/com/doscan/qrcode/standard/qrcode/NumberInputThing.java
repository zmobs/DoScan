package com.doscan.qrcode.standard.qrcode;

import java.util.regex.Pattern;

/**
 * 纯数字模式
 */
public class NumberInputThing extends InputThing {

    @Override
    public Byte[] getModeIndicator() {
        Byte[] indicator =  {0,0,0,1};
        return indicator;
    }

    @Override
    public boolean isMatch(String content) {
        Pattern pattern = Pattern.compile("^\\d+$");
        boolean result =  pattern
                .matcher(content)
                .matches();
        return result;
    }

    @Override
    public Byte[] getBits() {
        return new Byte[0];
    }

    @Override
    public String getName() {
        return "纯数字模式";
    }
}

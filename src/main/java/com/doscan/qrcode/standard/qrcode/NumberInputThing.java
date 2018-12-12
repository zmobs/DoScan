package com.doscan.qrcode.standard.qrcode;

import java.util.regex.Pattern;

/**
 * 纯数字模式
 */
public class NumberInputThing extends InputThing {

    @Override
    public boolean isMatch(String content) {
        Pattern pattern = Pattern.compile("^[0-9]$");
        boolean result =  pattern
                .matcher(content)
                .matches();
        return result;
    }

    @Override
    public int[] getBits() {
        return new int[0];
    }
}

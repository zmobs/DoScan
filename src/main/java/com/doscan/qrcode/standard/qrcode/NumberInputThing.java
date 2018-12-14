package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.proto.NumBitUnit;

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
    public Byte[] getBits(String content) {

        int numLength = content.length();
        int lastSubStart = numLength - (numLength % 3);

        for(int i = 0; i <= lastSubStart; i = i + 3){
            int subLength = numLength - i;
            if(subLength > 3){
                subLength = 3;
            }
            String subNum = content.substring(i,i + subLength);
            NumBitUnit numBitUnit = new NumBitUnit(subNum);
        }


        return new Byte[0];
    }

    @Override
    public String getName() {
        return "纯数字模式";
    }
}

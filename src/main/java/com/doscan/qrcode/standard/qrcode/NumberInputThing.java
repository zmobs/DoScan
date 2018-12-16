package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.proto.NumBitUnit;
import com.doscan.qrcode.util.HexUtil;
import com.doscan.qrcode.util.LogUtil;

import java.util.regex.Pattern;

/**
 * 纯数字模式
 */
public class NumberInputThing extends InputThing {

    @Override
    public BitArray getModeIndicator() {
        BitArray indicator = new BitArray();
        indicator.appendBit(false);
        indicator.appendBit(false);
        indicator.appendBit(false);
        indicator.appendBit(true);
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
    public BitArray getBits(String content) {

        int numCount = content.length();
        // 动态确定长度
        HexUtil.intToBinaryStr(numCount,10);


        int numLength = content.length();
        int lastSubStart = numLength - (numLength % 3);
        BitArray inputBits = new BitArray();
        for(int i = 0; i <= lastSubStart; i = i + 3){
            int subLength = numLength - i;
            if(subLength > 3){
                subLength = 3;
            }
            String subNum = content.substring(i,i + subLength);
            NumBitUnit numBitUnit = new NumBitUnit(subNum);
            inputBits.appendBitArray(numBitUnit.getBitArray());
            String numStrValue = numBitUnit.getCodeNumBin();
            LogUtil.log("numStrValue   ----   " + numStrValue);

        }
        LogUtil.log("inputBits   ----   " + inputBits.toString());

        return inputBits;
    }

    @Override
    public String getName() {
        return "纯数字模式";
    }
}

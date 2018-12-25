package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.proto.NumBitUnit;
import com.doscan.qrcode.standard.version.Version;
import com.doscan.qrcode.util.HexUtil;

import java.util.regex.Pattern;

/**
 * 纯数字模式
 */
public class NumberInputThing extends InputThing {

//    NumberInputThing(String input) {
//        super(input);
//    }
//    NumberInputThing(){
//
//    }

    @Override
    public BitArray getModeIndicator() {
        return HexUtil.strToBitArray("0001");
    }

    @Override
    public boolean isMatch() {
        Pattern pattern = Pattern.compile("^\\d+$");
        boolean result =  pattern
                .matcher(content)
                .matches();
        return result;
    }

    @Override
    public BitArray getBits() {
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
        }

        return inputBits;
    }

    @Override
    public String getName() {
        return "纯数字模式";
    }

    @Override
    public BitArray getCountLength(Version version) {
        int numCount = content.length();
        String hexStr = HexUtil.intToBinaryStr(numCount,version.getCharIndicatorCount(this));
        BitArray bits = HexUtil.strToBitArray(hexStr);
        return bits;
    }
}

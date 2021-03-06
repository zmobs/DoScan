package com.duqingquan.doscan.qrcode.standard.qrcode.input;

import com.duqingquan.doscan.qrcode.proto.BitArray;
import com.duqingquan.doscan.qrcode.proto.NumBitUnit;
import com.duqingquan.doscan.qrcode.standard.version.Version;
import com.duqingquan.doscan.qrcode.util.HexUtil;

import java.util.regex.Pattern;

/**
 * 纯数字模式
 */
public class NumberInputThing extends InputThing {


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

    /**
     * 获取容量对应的数量
     * @param bitNum
     * @return
     */
    @Override
    public int getCapNum(int bitNum){
        return (int)(bitNum * 0.3);
    }

    @Override
    public BitArray getBits() {
        int numLength = content.length();
        int lastSubStart = numLength - (numLength % 3);
        BitArray inputBits = new BitArray();
        for(int i = 0; i <= lastSubStart; i = i + 3){
            int subLength = numLength - i;
            if(subLength < 1){
                break;
            }
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

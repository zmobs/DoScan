package com.doscan.qrcode.util;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.proto.BitArray;

public class HexUtil {

    public static String intToBinaryStr(int num,int count){
        /** only 1-3 length str is accept**/
        int strCount = count;

        String codeNumBin = Integer.toBinaryString(num);
        int binCount = codeNumBin.length();
        StringBuilder stringBuilder = new StringBuilder(codeNumBin);
        for(int i = 0; i < (strCount - binCount); i++){
            stringBuilder.insert(0,"0");
        }
        String result = stringBuilder.toString();
        return result;
    }


    public static BitArray strToBitArray(String bitStr){

        if(StringUtil.isEmpty(bitStr)){
            return null;
        }
        BitArray bitArray = new BitArray();
        int length = bitStr.length();
        for(int i = 0; i < length; i++){
            char perChar = bitStr.charAt(i);
            if(perChar == '1'){
                bitArray.appendBit(true);
            }else if(perChar == '0'){
                bitArray.appendBit(false);
            }else {
                // bomb
                throw new BombException("just 1 or 0 is accept");
            }
        }

        return bitArray;
    }


}

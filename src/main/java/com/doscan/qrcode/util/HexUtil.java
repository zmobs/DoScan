package com.doscan.qrcode.util;

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


}

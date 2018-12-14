package com.doscan.qrcode.proto;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.util.HexUtil;
import com.doscan.qrcode.util.LogUtil;
import com.doscan.qrcode.util.StringUtil;

public class NumBitUnit {

    private String content;
    private Byte[] bits = new Byte[0];



    public NumBitUnit(String content){
        if(StringUtil.isEmpty(content)){
            throw new BombException("empty text");
        }
        if(content.length() > 3){
            throw new BombException("num text is too long");
        }
        this.content = content;
        initBitInfo();
    }


    private void initBitInfo(){
        String codeNumBin = intToBinaryStr(content);
        int bitLength = codeNumBin.length();
        bits = new Byte[bitLength];
        for(int i = 0; i < bitLength;i++){
            if(codeNumBin.charAt(i) == '1'){
                bits[i] = 1;
            }else{
                bits[i] = 0;
            }
        }
    }



    private String intToBinaryStr(String num){
        /** only 1-3 length str is accept**/
        int strCount = 10;
        int numLength = num.length();

        if(numLength == 3){
            strCount = 10;
        }else if(numLength == 2){
            strCount = 7;
        }else {
            strCount = 4;
        }

        String codeNumBin = Integer.toBinaryString(Integer.parseInt(num)
        );
        int binCount = codeNumBin.length();
        StringBuilder stringBuilder = new StringBuilder(codeNumBin);
        for(int i = 0; i < (strCount - binCount); i++){
            stringBuilder.insert(0,"0");
        }
        String result = stringBuilder.toString();
        return result;
    }

}

package com.duqingquan.doscan.qrcode.proto;

import com.duqingquan.doscan.qrcode.exception.BombException;
import com.duqingquan.doscan.qrcode.util.StringUtil;

public class NumBitUnit {

    /**
     * 原始数字字符串
     */
    private String content;
    /**
     * 比特流的比特组件容器
     */
    BitArray bitArray = new BitArray();
    /**
     * 比特流的字符串体现
     */
    String codeNumBin;



    public BitArray getBitArray() {
        return bitArray;
    }

    public String getCodeNumBin() {
        return codeNumBin;
    }

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

        bitArray.clear();
        codeNumBin = intToBinaryStr(content);
        int bitLength = codeNumBin.length();

        for(int i = 0; i < bitLength;i++){
            if(codeNumBin.charAt(i) == '1'){
                bitArray.appendBit(true);
            }else{
                bitArray.appendBit(false);
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

        String codeNumBin = Integer.toBinaryString(Integer.parseInt(num));
        int binCount = codeNumBin.length();
        StringBuilder stringBuilder = new StringBuilder(codeNumBin);
        for(int i = 0; i < (strCount - binCount); i++){
            stringBuilder.insert(0,"0");
        }
        String result = stringBuilder.toString();
        return result;
    }

}

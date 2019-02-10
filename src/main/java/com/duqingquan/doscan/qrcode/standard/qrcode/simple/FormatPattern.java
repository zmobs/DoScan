package com.duqingquan.doscan.qrcode.standard.qrcode.simple;

import com.duqingquan.doscan.qrcode.proto.BitArray;
import com.duqingquan.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.duqingquan.doscan.qrcode.standard.table.DotTable;
import com.duqingquan.doscan.qrcode.util.HexUtil;

public class FormatPattern {

    ErrorCorrectLevel ecLevel;

    public FormatPattern(ErrorCorrectLevel errorCorrectLevel){
        ecLevel = errorCorrectLevel;
    }


    private void actualEmbed(byte[][] tempData,int mask){

        BitArray bits = generateBCH(ecLevel,mask);
        // 2015 新规定，进行xor操作
        BitArray xorBits = HexUtil.strToBitArray("101010000010010");
        bits.xor(xorBits);
        int sideNum = tempData.length - 1;
        // 对临时数据，进行format信息布局操作
        for(int i = 0; i < 15;i++){

            byte peerByte = (byte) (bits.get(i) ? 1 : 0);
            if(i < 8){
                if(i < 6){
                    tempData[8][i] = peerByte;
                    tempData[sideNum - i][8] = peerByte;
                }else{
                    tempData[8][i + 1] = peerByte;
                    tempData[sideNum - i][8] = peerByte;
                }

            }else{
                // 8 - 15
                if(i < 9){
                    tempData[7][8] = peerByte;
                    tempData[8][sideNum - 6 + i - 8] = peerByte;
                }else{
                    tempData[14 - i][8] = peerByte;
                    tempData[8][sideNum - 6 + i - 8] = peerByte;
                }
            }

        }

    }

    public void embDataWithMash(DotTable dotTable, int mask){
        byte[][] tempData = dotTable.getData();
        actualEmbed(tempData,mask);
    }

    public void embDataWithMash(byte[][] tempData, int mask){
        actualEmbed(tempData,mask);
    }


    public void placeHold(DotTable dotTable){


        byte[][] data = dotTable.getData();
        int maxIndex = dotTable.getSideSize() - 1;
        for(int i = 0; i < 9;i++){
            if(i!= 6){
                data[8][i] = -2;
                data[i][8] = -2;
            }
            if(i < 8){
                data[maxIndex - i][8] = -2;
                if(i != 7){
                    data[8][maxIndex - i] = -2;
                }

            }
        }
    }

    static int findMSBSet(int value) {
        return 32 - Integer.numberOfLeadingZeros(value);
    }

    private BitArray generateBCH(ErrorCorrectLevel errorCorrectLevel,int maskPattern){
        //101010000010010
        // bch 的生成多项式系数 标准规定的
        final int BCH_BASE = 0x537;

        String srcBitStr = errorCorrectLevel.getValue() + HexUtil.intToBinaryStr(maskPattern,3);
        int srcBitNum = Integer.valueOf(srcBitStr,2);

        int value = srcBitNum;
        value <<= 10;
        // Do the division business using exclusive-or operations.
        while (findMSBSet(value) >= 11) {
            value ^= BCH_BASE << (findMSBSet(value) - 11);
        }
        // Now the "value" is the remainder (i.e. the BCH code)
        BitArray bitArray = new BitArray();
        bitArray.appendBits(srcBitNum,5);
        bitArray.appendBits(value,10);
        return bitArray;
    }

    public void place(DotTable dotTable, ErrorCorrectLevel errorCorrectLevel,int maskPattern){

        generateBCH(errorCorrectLevel,maskPattern);

        byte[][] data = dotTable.getData();
        int maxIndex = dotTable.getSideSize() - 1;
        for(int i = 0; i < 9;i++){
            if(i!= 6){
                data[8][i] = -2;
                data[i][8] = -2;
            }
            if(i < 8){
                data[maxIndex - i][8] = -2;
                if(i != 7){
                    data[8][maxIndex - i] = -2;
                }
            }
        }
    }



}

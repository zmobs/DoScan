package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.standard.version.Version;
import com.doscan.qrcode.util.HexUtil;
import com.doscan.qrcode.util.Log;

public class FormatPattern {

    ErrorCorrectLevel ecLevel;

    public FormatPattern(ErrorCorrectLevel errorCorrectLevel){
        ecLevel = errorCorrectLevel;
    }

    public void tempDataWithMash(byte[][] tempData,int mask){
        BitArray bits = generateBCH(ecLevel,mask);

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


    private BitArray generateBCH(ErrorCorrectLevel errorCorrectLevel,int maskPattern){
        //101010000010010
        // bch 的生成多项式系数 标准规定的
        final int BCH_BASE = 0x537;

        String srcBitStr = errorCorrectLevel.getValue() + HexUtil.intToBinaryStr(maskPattern,3);
        Log.d("srcBitStr  ----  " + srcBitStr);

        return null;
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

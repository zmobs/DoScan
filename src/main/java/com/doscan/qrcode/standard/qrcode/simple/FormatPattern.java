package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.standard.table.DotTable;

public class FormatPattern {

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

}

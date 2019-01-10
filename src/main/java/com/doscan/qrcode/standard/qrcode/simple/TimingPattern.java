package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.standard.version.Version;

public class TimingPattern {

    final int startIndex = 6;
    int endIndex;

    public TimingPattern(Version version) {
        int sideNum = version.getSideModuleNum();
        endIndex = sideNum - startIndex;
    }


    public void placeDots(DotTable dotTable){
        byte[][] data = dotTable.getData();
        for(int i = startIndex; i < endIndex;i++){
            if(i % 2 == 0){
                data[i][startIndex] = 1;
                data[startIndex][i] = 1;
            }else{
                data[i][startIndex] = 0;
                data[startIndex][i] = 0;
            }
        }
    }
}

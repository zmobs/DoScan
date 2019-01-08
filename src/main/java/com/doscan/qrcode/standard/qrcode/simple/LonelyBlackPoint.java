package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.standard.table.DotTable;

public class LonelyBlackPoint {
    public void place(DotTable table){
        byte[][] bytes = table.getData();
        int sideNum = table.getSideSize();
        bytes[8][sideNum - 8] = 1;
    }
}

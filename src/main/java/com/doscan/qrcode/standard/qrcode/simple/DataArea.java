package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.standard.table.DotTable;

import java.util.ArrayList;
import java.util.List;

public class DataArea {

    private List<Pos> borderPos = new ArrayList<>();


    public DataArea(DotTable dotTable) {
        int sideNum = dotTable.getSideSize();
        int endX = sideNum - 9;
        int endX2 = sideNum - 1;
        borderPos.add(new Pos(0,9));
        borderPos.add(new Pos(9,9));
        borderPos.add(new Pos(9,0));
        borderPos.add(new Pos(endX,0));
        borderPos.add(new Pos(endX,9));
        borderPos.add(new Pos(endX2,9));

    }

    class Pos{

        int x;
        int y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

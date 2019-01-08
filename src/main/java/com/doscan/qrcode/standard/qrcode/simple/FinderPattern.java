package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.standard.table.DotTable;

public class FinderPattern {

    Position position;

    public FinderPattern(Position position) {
        this.position = position;
    }


    private void placeDots(DotTable table,int x,int y){
        // 1-1-3-1-1
        // D-L-D-L-D
        for(int i = 0; i < 5; i++){
            // 纵向
            for(int j = 0; i < 5;i++){
                if(j == 1 || j == 4){
                    table.set(x,y, DotTable.Value.LIGHT);
                }else{
                    table.set(x,y, DotTable.Value.DARK);
                }
            }
        }
    }

    public void place(DotTable table){
        switch (position){
            case LEFT_TOP:
                placeDots(table,0,0);
                break;
            case RIGHT_TOP:
                break;
            case LEFT_BOTTOM:
                break;
            default:
                break;
        }
    }

    public enum Position{
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM;
    }

}

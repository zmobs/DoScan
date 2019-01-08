package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.standard.table.DotTable;

public class FinderPattern {

    Position position;

    public FinderPattern(Position position) {
        this.position = position;
    }


    private void placeDots(DotTable table,int x,int y){
        byte[][] bytes = table.getData();
        // 1-1-3-1-1
        // D-L-D-L-D
        for(int i = x; i < 7; i++){
            // 纵向
            for(int j = y; j < 7;j++){
                if(i == 0 || i == 6 || j == 0 || j == 6){
                  bytes[i][j] = 1;
                }else if((i >= 2 && i <= 4) && (j >= 2 && j <= 4)){
                    bytes[i][j] = 1;
                }else{
                    bytes[i][j] = 0;
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

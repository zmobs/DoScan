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
        for(int i = 0; i < 7; i++){
            // 纵向
            for(int j = 0; j < 7;j++){
                if(i == 0 || i == 6 || j == 0 || j == 6){
                  bytes[x + i][y + j] = 1;
                }else if((i >= 2 && i <= 4) && (j >= 2 && j <= 4)){
                    bytes[x + i][y + j] = 1;
                }else{
                    bytes[x + i][y + j] = 0;
                }
            }
        }
    }

    public void place(DotTable table){
        int sideSize = table.getSideSize();
        int borderStartPos = sideSize - 7;
        switch (position){
            case LEFT_TOP:
                placeDots(table,0,0);
                break;
            case RIGHT_TOP:
                // 单边长度 -7 -1
                placeDots(table,borderStartPos,0);
                break;
            case LEFT_BOTTOM:
                placeDots(table,0,borderStartPos);
                break;
            default:
                break;
        }
    }

    /**
     * 定位符号的三个可能位置
     */
    public enum Position{
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM
    }

}

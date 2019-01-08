package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.standard.table.DotTable;

public class SeparatorFP {

    FinderPattern finderPattern;

    SeparatorFP(FinderPattern finderPattern){
        this.finderPattern = finderPattern;
    }

    public void place(DotTable table){

        byte[][] bytes = table.getData();
        int sideNum = table.getSideSize() - 1;
        int startSPNum = sideNum - 7;
        switch (finderPattern.position){
            case LEFT_TOP:
                for(int i = 0;i < 8;i++){
                    bytes[7][i] = 0;
                }
                for(int i = 0;i < 8;i++){
                    bytes[i][7] = 0;
                }
                break;
            case RIGHT_TOP:
                for(int i = sideNum;i > startSPNum - 1;i--){
                    bytes[i][7] = 0;
                }
                for(int i = 0;i < 8;i++){
                    bytes[startSPNum][i] = 0;
                }
                break;

            case LEFT_BOTTOM:

                for(int i = 0;i < 8;i++){
                    bytes[i][startSPNum] = 0;
                }
                for(int i = sideNum;i > startSPNum;i--){
                    bytes[7][i] = 0;
                }

                break;

                default:
                    break;
        }
    }
}

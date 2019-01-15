package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.standard.table.AlignmentTable;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class AlignmentPattern {

    int[] dotInfo;
    AlignmentPattern(int version){
        dotInfo = AlignmentTable.disForVersion(version);
    }


    public void placeDots(DotTable table){

        ArrayList<Coordinate> nums = new ArrayList<>(32);
        int dotNum = 0;

        for(int i = 0; i < dotInfo.length;i++){
            if(dotInfo[i] < 0){
                break;
            }
            dotNum += 1;
        }

        if(dotNum == 2){
            //  version 1- 6
            nums.add(new Coordinate(dotInfo[1],dotInfo[1]));
        }else{
            for(int i = 0; i < dotInfo.length;i++){
                if(dotInfo[i] < 0){
                    break;
                }
                for(int j = 0; j < dotInfo.length;j++){
                    if(dotInfo[j] < 0){
                        break;
                    }
                    if(i == 0 && j == 0){
                        continue;
                    } else if(i == 0 && j == dotNum - 1) {
                        continue;
                    }else if(i == dotNum - 1 && j == 0) {
                        continue;
                    }else {
                        nums.add(new Coordinate(dotInfo[i],dotInfo[j]));
                    }
                }
            }
        }
        // 1:1:1  黑白黑色块绘制
        byte[][] data = table.getData();
        // 进行绘制
        for(Coordinate coordinate :  nums){
            // 最中心
            data[coordinate.x][coordinate.y] = 1;
            // 外围一层 白色
            for(int i = coordinate.x - 1 ; i <= coordinate.x + 1; i++){
                for(int j = coordinate.y - 1; j <= coordinate.y + 1;j++){
                    if(i == coordinate.x && j == coordinate.y){
                        continue;
                    }
                    data[i][j] = 0;
                }
            }
            // 最外围一层 黑色
            for(int i = coordinate.x - 2 ; i <= coordinate.x + 2; i++){
                for(int j = coordinate.y - 2; j <= coordinate.y + 2;j++){
                    if(i == coordinate.x - 2 || i == coordinate.x + 2){
                        data[i][j] = 1;
                    }else if(j == coordinate.y - 2 || j == coordinate.y + 2){
                        data[i][j] = 1;
                    }
                }
            }
        }

    }

    class Coordinate{
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}

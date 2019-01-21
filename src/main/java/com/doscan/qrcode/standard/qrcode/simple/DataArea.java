package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据编码区域
 */
public class DataArea {


    int sideNum;
    byte[][] dataTable;

    public DataArea() {
        dataTable = new byte[1][1];
    }

    public void place(DotTable table, BitArray bits) {

        sideNum = table.getSideSize();
        dataTable = new byte[sideNum][sideNum];
        for(int i = 0 ; i < sideNum; i++){
            for(int j = 0; j < sideNum;j++){
                dataTable[i][j] = -1;
            }
        }

        int bitLen = bits.getSize();
        byte[][] dotData = table.getData();
        // 默认从角落升序
        int placeOrientation = 1;

        // 按照指定的排列规则，推演出下一个比特应该存在的位置
        int m = sideNum - 1;
        int n = sideNum - 1;

        int firstCol = m;
        int secondCol = m - 1;
        int i = 0;

        int preI = -1;
        while (true) {
            if (m < 0 || i >= bitLen) {
                break;
            }

            byte perByte = bits.get(i) ? (byte) 1 : 0;
            if(i != preI){
                Log.d("perByte  ------ " + perByte);
                preI = i;
            }

//            if(i > 7){
//                break;
//            }
            // 迭代规则编写
            if (dotData[m][n] == -1) {
                //  -1 是未使用状态
//                Log.d("m  ------ " + m + "    ----n -----   " + n + "        perByte  ----  " + perByte);
                dotData[m][n] = perByte;
                dataTable[m][n] = perByte;
                i++;
                continue;
            } else {
                // 该位置已经被其他区域使用了，单独存一下他们得坐标

            }

            // 寻找下一个可放置位置的算法实现
            if (m == firstCol) {
                // 不论任何场景 第一列永远不会发生转折
                m = secondCol;
                continue;
            }

            if (placeOrientation == 1) {
                // up
                if (m == secondCol && n == 0) {

                    // 到达了当前列编码位置的终止部分，则需要转换方向
                    placeOrientation = 0;

                    // 整体向下挪2列
                    firstCol -= 2;
                    if (firstCol == 6) {
                        firstCol -= 1;
                    }
                    secondCol = firstCol - 1;
                    if (secondCol == 6) {
                        secondCol -= 1;
                    }

                    m = firstCol;

                } else {
                    //  第二列，而且尚未到达顶部
                    n -= 1;
                    m = firstCol;
                }
            } else {
                // down
                if (m == secondCol && n == sideNum - 1) {


                    // 到达了当前列编码位置的终止部分，则需要转换方向
                    placeOrientation = 1;

                    // 整体向下挪2列
                    firstCol -= 2;
                    if (firstCol == 6) {
                        firstCol -= 1;
                    }
                    secondCol = firstCol - 1;
                    if (secondCol == 6) {
                        secondCol -= 1;
                    }
                    m = firstCol;
                } else {
                    //  第二列，而且尚未到达顶部
                    n += 1;
                    m = firstCol;
                }
            }
        }

        Log.d(" i  --- " + i);

    }

}

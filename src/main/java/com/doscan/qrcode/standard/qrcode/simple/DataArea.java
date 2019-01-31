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

    public byte[][] getDataTable(){
        return dataTable;
    }

    public DataArea() {
        dataTable = new byte[1][1];
    }

    public void placeAnim(DotTable dotTable) {

        byte[][] animData = dotTable.getData();
        // 默认从角落升序
        int placeOrientation = 1;

        // 按照指定的排列规则，推演出下一个比特应该存在的位置
        int m = sideNum - 1;
        int n = sideNum - 1;

        int firstCol = n;
        int secondCol = n - 1;


        while (true) {


            if (m < 0 || n < 0) {
                break;
            }

            // 迭代规则编写
            if (dataTable[m][n] != -1) {
                //  -1 是未使用状态,其他组件已经占位的前提下，上下的都是合法的数据区域
                animData[m][n] = dataTable[m][n];

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // 该位置已经被其他区域使用了，单独存一下他们得坐标
            }



            // 寻找下一个可放置位置的算法实现
            if (n == firstCol) {
                // 不论任何场景 第一列永远不会发生转折
                n = secondCol;
                continue;
            }

            if (placeOrientation == 1) {
                // 向左
                if (n == secondCol && m == 0) {

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
                    n = firstCol;
                } else {
                    //  第二列，而且尚未到达顶部
                    m -= 1;
                    n = firstCol;
                }
            } else {
                // 向右
                if (n == secondCol && m == sideNum - 1) {

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
                    n = firstCol;
                } else {
                    //  第二列，而且尚未到达顶部
                    m += 1;
                    n = firstCol;
                }
            }
        }

    }

    public void place(DotTable table, BitArray bits) {

        sideNum = table.getSideSize();
        dataTable = new byte[sideNum][sideNum];
        for (int i = 0; i < sideNum; i++) {
            for (int j = 0; j < sideNum; j++) {
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

        int firstCol = n;
        int secondCol = n - 1;
        int i = 0;


        while (true) {

            //|| i >= bitLen
            if (m < 0 || n < 0) {
                break;
            }

            byte perByte = 0;
            if (i >= bitLen) {
                // Log.d("aaa  -----  " + i + "    mmm ---  " + m + "   n ---  " + n);
            } else {
                perByte = bits.get(i) ? (byte) 1 : 0;
            }

            // 迭代规则编写
            if (dotData[m][n] == -1) {
                //  -1 是未使用状态,其他组件已经占位的前提下，上下的都是合法的数据区域
                dotData[m][n] = perByte;
                dataTable[m][n] = perByte;
                i++;
                continue;
            } else {
                // 该位置已经被其他区域使用了，单独存一下他们得坐标

            }

            // 寻找下一个可放置位置的算法实现
            if (n == firstCol) {
                // 不论任何场景 第一列永远不会发生转折
                n = secondCol;
                continue;
            }

            if (placeOrientation == 1) {
                // 向左
                if (n == secondCol && m == 0) {

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

                    n = firstCol;

                } else {
                    //  第二列，而且尚未到达顶部
                    m -= 1;
                    n = firstCol;
                }
            } else {
                // 向右
                if (n == secondCol && m == sideNum - 1) {

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
                    n = firstCol;
                } else {
                    //  第二列，而且尚未到达顶部
                    m += 1;
                    n = firstCol;
                }
            }
        }


    }

}

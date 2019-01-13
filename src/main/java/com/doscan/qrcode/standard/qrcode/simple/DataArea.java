package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.table.DotTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据编码区域
 */
public class DataArea {


    int sideNum;
    public DataArea() {

    }

    public void place(DotTable table, BitArray bits){
        sideNum = table.getSideSize();
        int bitLen = bits.getSize();
        byte[][] dotData = table.getData();
        int placeOrientation = 1;

        for(int i = 0 ; i < bitLen; ){

            byte perByte = bits.get(i) ? (byte) 1 : 0;
            // 按照指定的排列规则，推演出下一个比特应该存在的位置
            int codewordIndex = 0;
            int m;
            int n;
            m = n = sideNum - 1;
            int firstCol = m;
            int secondCol = m - 1;

            int preM;
            int preN;
            // 当前的码字方向
            int order = 1;
            for(; m >= 0; ){

                // 迭代规则编写

                if(dotData[m][n] == -1){
                        //  -1 是未使用状态
                        dotData[m][n] = perByte;
                        i++;
                        codewordIndex += 1;
                        preM = m;
                        preN = n;
                }else{

                }
                if(order == 1){
                    // up
                    if(m == secondCol && m == 0){
                        // 到达了当前列编码位置的终止部分，则需要转换方向
                        order = 0;

                        // 整体向下挪2列
                        firstCol -= 1;
                        if(firstCol == 6){
                            firstCol -= 1;
                        }
                        secondCol -= firstCol -1;
                        if(secondCol == 6){
                            secondCol -= 1;
                        }

                    }else if(m == firstCol){
                        m = secondCol;
                    } else{
                        //  第二列，而且尚未到达顶部
                        m -= 1;
                    }
                }else{
                    // down
                    //

                    if(m == secondCol && m == sideNum - 1){
                        // 到达了当前列编码位置的终止部分，则需要转换方向
                        order = 1;

                        // 整体向下挪2列
                        firstCol -= 1;
                        if(firstCol == 6){
                            firstCol -= 1;
                        }
                        secondCol -= firstCol -1;
                        if(secondCol == 6){
                            secondCol -= 1;
                        }

                    }else if(m == firstCol){
                        m = secondCol;
                    } else{
                        //  第二列，而且尚未到达顶部
                        m += 1;
                    }
                }


            }
        }

    }

}

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
            int m,n;
            int firstCol = m;
            int secondCol = m - 1;

            int preM;
            int preN;
            int order = 1;
            for(m = n = sideNum - 1; m >= 0; ){

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
                    if(m == firstCol){
                        m = secondCol;
                    }else{

                    }
                }else{
                    // down
                }

                // 需要先分块，按照每个模块的数值来计算  57页

//                for(int n = sideNum - 1; n >= 0; ) {
//
//                    // 成功赋值一位
//                    if (dotData[m][n] == -1) {
//
//                    }
//
//                    // 以及存在其他区域的数据，寻找下一个的算法实现
//                    if (n < 0) {
//                        m -= 2;
//                        n = sideNum;
//                    }
//
//                    if(codewordIndex > 7){
//                        codewordIndex = 0;
//                    }
//                }
//            }
        }

    }

}

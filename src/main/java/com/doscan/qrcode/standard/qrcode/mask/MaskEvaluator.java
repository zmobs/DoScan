package com.doscan.qrcode.standard.qrcode.mask;


import com.doscan.qrcode.standard.qrcode.simple.FormatPattern;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.util.Log;

/**
 * 编码区域得遮罩层评估
 */
public class MaskEvaluator {

    FormatPattern formatPattern;

    public MaskEvaluator(FormatPattern formatPattern){
        this.formatPattern = formatPattern;
    }

    public int evaluateMask(byte[][] dotTable,byte[][] dataTable){


        /**
         * 最小得坏点数值
         */
        int minPenalty = Integer.MAX_VALUE;
        /**
         * 最好得遮罩模式
         */
        int bestMaskPattern = -1;

        /**
         * 二维码得中默认0-7 8个遮罩模式
         */
        for(int i = 1; i < 2;i++){
            int penalty = calculateMaskPenalty(dotTable,dataTable,i);
            if(penalty < minPenalty){
                minPenalty = penalty;
                bestMaskPattern = i;
            }
        }
        return bestMaskPattern;
    }

    /**
     * 测试用的代码
     * @param dataTable
     * @param mask
     */
    public void mockMask(byte[][] dataTable,int mask){

        int sideNum = dataTable.length;
        for(int i = 0 ; i < sideNum; i++){
            for(int j = 0; j < sideNum;j++){

                if(dataTable[i][j] == -1){
                    // 只处理数据区域，但是评估时，需要评估整体区域
                    switch (mask){
                        case 0x00:
                            // 0
                            if((i + j) % 2 == 0){
                                // 该位置为黑色
                                reverse(dataTable,i,j);
                            }
                            break;
                        case 0x01:
                            // 0
                            if(j % 2 == 0){
                                // 该位置为黑色
                                reverse(dataTable,i,j);
                            }
                            break;
                        case 0x02:
                            // 0
                            if(j % 3 == 0){
                                // 该位置为黑色
                                reverse(dataTable,i,j);
                            }
                            break;
                        case 0x03:
                            // 0
                            if((i + j) % 3 == 0){
                                // 该位置为黑色
                                reverse(dataTable,i,j);
                            }
                            break;
                        case 0x04:
                            // 0
                            if((j / 2 + i / 3) % 2 == 0){
                                // 该位置为黑色
                                reverse(dataTable,i,j);
                            }
                            break;
                        case 0x05:
                            // 0
                            if(((i * j) % 2) + (i * j % 3)== 0){
                                // 该位置为黑色
                                reverse(dataTable,i,j);
                            }
                            break;
                        case 0x06:
                            // 0
                            if((((i * j) % 2) + (i * j % 3)) % 2== 0){
                                // 该位置为黑色
                                reverse(dataTable,i,j);
                            }
                            break;
                        case 0x07:
                            // 0
                            if((((i + j) % 2) + (i * j % 3)) % 2== 0){
                                // 该位置为黑色
                                reverse(dataTable,i,j);
                            }
                            break;
                        default:

                            break;

                    }
                }
            }
        }
    }

    /**
     * 对数据区域进行整体换算
     * @param dataTable
     * @return
     */
    private int calculateMaskPenalty(byte[][] dotTable,byte[][] dataTable,int mask) {

        int dataSideNum = dotTable.length;

        byte[][] tempData = new byte[dataSideNum][dataSideNum];
        for(int i = 0; i < dataSideNum;i++){
            for(int j = 0; j < dataSideNum;j++){
                tempData[i][j] = dotTable[i][j];
            }
        }

        for(int i = 0 ; i < dataSideNum; i++){
            for(int j = 0; j < dataSideNum;j++){

                if(dataTable[i][j] != -1){
                    // 只处理数据区域，但是评估时，需要评估整体区域
                    switch (mask){
                        case 0x00:
                            // 0
                            if((i + j) % 2 == 0){
                                // 该位置为黑色
                                if(tempData[i][j] == 0){
                                    tempData[i][j] = 0;
                                }else{
                                    tempData[i][j] = 1;
                                }
                            }else{
                                // 该位置是白色
                                if(tempData[i][j] == 1){
                                    tempData[i][j] = 0;
                                }else{
                                    tempData[i][j] = 1;
                                }
                            }
                            break;
                        case 0x01:
                            // 0
                            if(j % 2 == 0){
                                // 该位置为黑色
                                reverse(tempData,i,j);
                            }
                            break;
                        case 0x02:
                            // 0
                            if(j % 3 == 0){
                                // 该位置为黑色
                                if(tempData[i][j] == 0){
                                    tempData[i][j] = 1;
                                }else{
                                    tempData[i][j] = 0;
                                }
                            }else{
                                if(tempData[i][j] == 1){
                                    tempData[i][j] = 1;
                                }else{
                                    tempData[i][j] = 0;
                                }
                            }
                            break;
                        case 0x03:
                            // 0
                            if((i + j) % 3 == 0){
                                // 该位置为黑色

                                reverse(tempData,i,j);
                            }
                            break;
                        case 0x04:
                            // 0
                            if((j / 2 + i / 3) % 2 == 0){
                                // 该位置为黑色
                                reverse(tempData,i,j);
                            }
                            break;
                        case 0x05:
                            // 0
                            if(((i * j) % 2) + (i * j % 3)== 0){
                                // 该位置为黑色
                                reverse(tempData,i,j);
                            }
                            break;
                        case 0x06:
                            // 0
                            if((((i * j) % 2) + (i * j % 3)) % 2== 0){
                                // 该位置为黑色
                                reverse(tempData,i,j);
                            }
                            break;
                        case 0x07:
                            // 0
                            if((((i + j) % 2) + (i * j % 3)) % 2== 0){
                                // 该位置为黑色
                                reverse(tempData,i,j);
                            }
                            break;
                        default:

                            break;

                    }
                }
            }
        }

        Log.d("maskPattern   ---  " + mask);
        // 根据已经选择的mask 对formatinfo 进行补充
        formatPattern.tempDataWithMash(tempData,mask);


//        // 对已经进行xor 整体数据进行 评分操作
//        //dotTable
//        int maxColNum = dataSideNum - 5;
//        int maxDotNum = dataSideNum - 2;
//        int preN1X = -1;
//        int preN1Y = -1;
//
//        int N1 = 0;
//        int N2 = 0;
//
//
//        for(int i = 0 ; i < dataSideNum; i++) {
//
//            for (int j = 0; j < dataSideNum; j++) {
//
//                // 横纵坐标
//                if(i <= maxColNum && i > preN1X){
//                    // 计算横向连续同色模点
//                    int m = i +1;
//                    while(tempData[m][j] == tempData[i][j]){
//                        m += 1;
//                        if(m >= dataSideNum){
//                            m = dataSideNum -1;
//                            break;
//                        }
//                    }
//                    // 走到这里，就是不同色了，进行判断
//                    int n1 = m - i;
//                    if(n1 > 4){
//
//                        N1 += n1;
//                        preN1X = m;
//                    }
//                }
//
//                if(j <= maxColNum && j > preN1Y){
//                    // 计算横向连续同色模点
//                    int m = j + 1;
//                    while(tempData[i][m] == tempData[i][j]){
//                        m += 1;
//                        if(m >= dataSideNum){
//                            m = dataSideNum -1;
//                            break;
//                        }
//                    }
//                    // 走到这里，就是不同色了，进行判断
//                    int n1 = m - j;
//                    if(n1 > 4){
//                        N1 += n1;
//                        preN1Y = m;
//                    }
//                }
//
//
//                // 方块检测函数
//                if(i <= maxDotNum && j <= maxDotNum){
//                    int dot1 = tempData[i][j];
//                    int dot2 = tempData[i+1][j+1];
//                    int dot3 = tempData[i+1][j];
//                    int dot4 = tempData[i][j + 1];
//                    if(dot1 == dot2 && dot2 == dot3 && dot3 == dot4){
//                        N2 += 1;
//                    }
//                }
//
//                // 检测 1：1：3：1：1
//                if(i < dataSideNum - 7 && j < dataSideNum - 7){
//
//                    int dot1 = tempData[i][j];
//                    int dot2 = tempData[i+1][j+1];
//                    int dot3 = tempData[i+1][j];
//                    int dot4 = tempData[i][j + 1];
//                    if(dot1 == dot2 && dot2 == dot3 && dot3 == dot4){
//                        N2 += 1;
//                    }
//                }
//
//
//            }
//        }
//
//        // 临时给界面绘制出来 测试代码 fixme
        for(int i = 0; i < dataSideNum;i++){
            for(int j = 0; j < dataSideNum;j++){
                dotTable[i][j] = tempData[i][j];
            }
        }
//
//        Log.d("mask  ----  " + mask);
//        Log.d("N1  ----  " + N1);
//        Log.d("N2  ----  " + N2);
        return 0;
    }



    private void reverse(byte[][] dotTable,int i,int j){

        if(dotTable[i][j] == 0){
            // 数据位白色
            dotTable[i][j] = 1;
        }else if(dotTable[i][j] == 1){
            // 数据位黑色
            dotTable[i][j] = 0;
        }

    }
}

package com.doscan.qrcode.standard.qrcode.mask;


import com.doscan.qrcode.standard.table.DotTable;

/**
 * 编码区域得遮罩层评估
 */
public class MaskEvaluator {

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
        for(int i = 0; i < 8;i++){
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

        int sideNum = dataTable.length;
        for(int i = 0 ; i < sideNum; i++){
            for(int j = 0; j < sideNum;j++){

                if(dataTable[i][j] != -1){
                    // 只处理数据区域，但是评估时，需要评估整体区域
                    switch (mask){
                        case 0x00:
                            // 0
                            if((i + j) % 2 == 0){
                                // 该位置为黑色
                                reverse(dotTable,i,j);
                            }
                            break;
                        case 0x01:
                            // 0
                            if(j % 2 == 0){
                                // 该位置为黑色
                                reverse(dotTable,i,j);
                            }
                            break;
                        case 0x02:
                            // 0
                            if(j % 3 == 0){
                                // 该位置为黑色
                                reverse(dotTable,i,j);
                            }
                            break;
                        case 0x03:
                            // 0
                            if((i + j) % 3 == 0){
                                // 该位置为黑色
                                reverse(dotTable,i,j);
                            }
                            break;
                        case 0x04:
                            // 0
                            if((j / 2 + i / 3) % 2 == 0){
                                // 该位置为黑色
                                reverse(dotTable,i,j);
                            }
                            break;
                        case 0x05:
                            // 0
                            if(((i * j) % 2) + (i * j % 3)== 0){
                                // 该位置为黑色
                                reverse(dotTable,i,j);
                            }
                            break;
                        case 0x06:
                            // 0
                            if((((i * j) % 2) + (i * j % 3)) % 2== 0){
                                // 该位置为黑色
                                reverse(dotTable,i,j);
                            }
                            break;
                        case 0x07:
                            // 0
                            if((((i + j) % 2) + (i * j % 3)) % 2== 0){
                                // 该位置为黑色
                                reverse(dotTable,i,j);
                            }
                            break;
                        default:

                            break;

                    }
                }
            }
        }
        // 对已经进行xor 整体数据进行 评分操作
        //dotTable
        for(int i = 0 ; i < sideNum; i++) {
            for (int j = 0; j < sideNum; j++) {

                if (dataTable[i][j] != -1) {

                }

            }
        }
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

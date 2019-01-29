package com.doscan.qrcode.standard.qrcode.mask;


import com.doscan.qrcode.standard.qrcode.simple.FormatPattern;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.util.Log;

/**
 * 编码区域得遮罩层评估
 */
public class MaskEvaluator {

    FormatPattern formatPattern;

    public MaskEvaluator(FormatPattern formatPattern) {
        this.formatPattern = formatPattern;
    }



    public void embedMask(byte[][] dotTable, byte[][] dataTable,int mask){

        int dataSideNum = dotTable.length;
        Log.d("mask   ----  " + mask);
        for (int i = 0; i < dataSideNum; i++) {
            for (int j = 0; j < dataSideNum; j++) {

                int honyVal = ((i * j) % 2) + (i * j % 3);

                if (dataTable[i][j] != -1) {
                    // 只处理数据区域，但是评估时，需要评估整体区域
                    switch (mask) {
                        case 0x00:
                            // 0
                            if ((i + j) % 2 == 0) {
                                // 该位置为黑色
                                reverse(dotTable, i, j);
                            }
                            break;
                        case 0x01:
                            // 1
                            if ((i & 0x1) == 0) {
                                // 该位置为黑色
                                reverse(dotTable, i, j);
                            }
                            break;
                        case 0x02:
                            // 2
                            if (i % 3 == 0) {
                                // 该位置为黑色
                                reverse(dotTable, i, j);
                            }
                            break;
                        case 0x03:
                            // 0
                            if ((i + j) % 3 == 0) {
                                // 该位置为黑色
                                reverse(dotTable, i, j);
                            }
                            break;
                        case 0x04:
                            // 0
                            if ((i / 2 + j / 3) % 2 == 0) {
                                // 该位置为黑色
                                reverse(dotTable, i, j);
                            }
                            break;
                        case 0x05:
                            // 0
                            if (honyVal == 0) {
                                // 该位置为黑色
                                reverse(dotTable, i, j);
                            }
                            break;
                        case 0x06:
                            // 0
                            if (honyVal % 2 == 0) {
                                // 该位置为黑色
                                reverse(dotTable, i, j);
                            }
                            break;
                        case 0x07:
                            // 0
                            if ((((i + j) % 2) + (i * j % 3)) % 2 == 0) {
                                // 该位置为黑色
                                reverse(dotTable, i, j);
                            }
                            break;
                        default:

                            break;

                    }
                }
            }
        }

        // 根据已经选择的mask 对formatinfo 进行补充
        formatPattern.tempDataWithMash(dotTable, mask);


    }
    public int evaluateMask(byte[][] dotTable, byte[][] dataTable) {


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
        for (int i = 0; i < 8; i++) {
            int penalty = calculateMaskPenalty(dotTable, dataTable, i);
            if (penalty < minPenalty) {
                minPenalty = penalty;
                bestMaskPattern = i;
            }
        }
        return bestMaskPattern;
    }


    /**
     * 对数据区域进行整体换算
     *
     * @param dataTable
     * @return
     */
    private int calculateMaskPenalty(byte[][] dotTable, byte[][] dataTable, int mask) {

        int dataSideNum = dotTable.length;

        byte[][] tempData = new byte[dataSideNum][dataSideNum];
        for (int i = 0; i < dataSideNum; i++) {
            for (int j = 0; j < dataSideNum; j++) {
                tempData[i][j] = dotTable[i][j];
            }
        }

        for (int i = 0; i < dataSideNum; i++) {
            for (int j = 0; j < dataSideNum; j++) {

                int honyVal = ((i * j) % 2) + (i * j % 3);
                if (dataTable[i][j] != -1) {
                    // 只处理数据区域，但是评估时，需要评估整体区域
                    switch (mask) {
                        case 0x00:
                            // 0
                            if ((i + j) % 2 == 0) {
                                // 该位置为黑色
                                reverse(tempData, i, j);
                            }
                            break;
                        case 0x01:
                            // 1
                            if ((i & 0x1) == 0) {
                                // 该位置为黑色
                                reverse(tempData, i, j);
                            }
                            break;
                        case 0x02:
                            // 2
                            if (i % 3 == 0) {
                                // 该位置为黑色
                                reverse(tempData, i, j);
                            }
                            break;
                        case 0x03:
                            // 0
                            if ((i + j) % 3 == 0) {
                                // 该位置为黑色
                                reverse(tempData, i, j);
                            }
                            break;
                        case 0x04:
                            // 0
                            if ((i / 2 + j / 3) % 2 == 0) {
                                // 该位置为黑色
                                reverse(tempData, i, j);
                            }
                            break;
                        case 0x05:
                            // 0
                            if (honyVal == 0) {
                                // 该位置为黑色
                                reverse(tempData, i, j);
                            }
                            break;
                        case 0x06:
                            // 0
                            if (honyVal % 2 == 0) {
                                // 该位置为黑色
                                reverse(tempData, i, j);
                            }
                            break;
                        case 0x07:
                            // 0
                            if ((((i + j) % 2) + (i * j % 3)) % 2 == 0) {
                                // 该位置为黑色
                                reverse(tempData, i, j);
                            }
                            break;
                        default:

                            break;

                    }
                }
            }
        }

        // 根据已经选择的mask 对formatinfo 进行补充
        formatPattern.tempDataWithMash(tempData, mask);

        // 对已经进行xor 整体数据进行 评分操作
        //dotTable
        int maxColNum = dataSideNum - 5;
        int maxDotNum = dataSideNum - 2;
        int preN1X = -1;
        int preN1Y = -1;

        int N1 = 0;
        int N2 = 0;
        int N3 = 0;
        int N4 = 0;
        int lightNum = 0;
        int darkNum = 0;

        for (int i = 0; i < dataSideNum; i++) {

            for (int j = 0; j < dataSideNum; j++) {

                // 横纵坐标
                if (i <= maxColNum && i > preN1X) {
                    // 计算横向连续同色模点
                    int m = i + 1;
                    while (tempData[m][j] == tempData[i][j]) {
                        m += 1;
                        if (m >= dataSideNum) {
                            m = dataSideNum - 1;
                            break;
                        }
                    }
                    // 走到这里，就是不同色了，进行判断
                    int n1 = m - i;
                    if (n1 > 4) {

                        N1 += n1;
                        preN1X = m;
                    }
                }

                if (j <= maxColNum && j > preN1Y) {
                    // 计算横向连续同色模点
                    int m = j + 1;
                    while (tempData[i][m] == tempData[i][j]) {
                        m += 1;
                        if (m >= dataSideNum) {
                            m = dataSideNum - 1;
                            break;
                        }
                    }
                    // 走到这里，就是不同色了，进行判断
                    int n1 = m - j;
                    if (n1 > 4) {
                        N1 += n1;
                        preN1Y = m;
                    }
                }


                // 方块检测函数
                if (i <= maxDotNum && j <= maxDotNum) {
                    int dot1 = tempData[i][j];
                    int dot2 = tempData[i + 1][j + 1];
                    int dot3 = tempData[i + 1][j];
                    int dot4 = tempData[i][j + 1];
                    if (dot1 == dot2 && dot2 == dot3 && dot3 == dot4) {
                        N2 += 4;
                    }
                }


                // 横向判断
                if (j > 7 && j < dataSideNum - 10) {
                    // 先判断有没有 四个宽度的亮色模点
                    int lightDot1 = tempData[i][j];
                    int lightDot2 = tempData[i][j + 1];
                    int lightDot3 = tempData[i][j + 2];
                    int lightDot4 = tempData[i][j + 3];

                    // 横向判断
                    if (lightDot1 == 0 && lightDot2 == 0 && lightDot3 == 0 && lightDot4 == 0) {
                        // 存在四个亮点了，判断前后是否有七星阵

                        if (i > 14) {
                            // 既找前面也要找后面
                            // 纵向判断
                            int preDot1 = tempData[i][j - 1];
                            int preDot2 = tempData[i][j - 2];
                            int preDot3 = tempData[i][j - 3];
                            int preDot4 = tempData[i][j - 4];
                            int preDot5 = tempData[i][j - 5];
                            int preDot6 = tempData[i][j - 6];
                            int preDot7 = tempData[i][j - 7];

                            if (isSevenDotRight(preDot1, preDot2, preDot3, preDot4, preDot5, preDot6, preDot7)) {
                                N3 += 40;
                            }
                        }
                        // 只找后面的元素
                        int nextDot1 = tempData[i][j];
                        int nextDot2 = tempData[i][j + 1];
                        int nextDot3 = tempData[i][j + 2];
                        int nextDot4 = tempData[i][j + 3];
                        int nextDot5 = tempData[i][j + 4];
                        int nextDot6 = tempData[i][j + 5];
                        int nextDot7 = tempData[i][j + 6];

                        if (isSevenDotRight(nextDot1, nextDot2, nextDot3, nextDot4, nextDot5, nextDot6, nextDot7)) {
                            N3 += 40;
                        }

                    }

                }


                if (i > 7 && i < dataSideNum - 10) {

                    // 先判断有没有 四个宽度的亮色模点
                    int lightDot1 = tempData[i + 1][j];
                    int lightDot2 = tempData[i + 2][j];
                    int lightDot3 = tempData[i + 3][j];
                    int lightDot4 = tempData[i + 4][j];

                    // 横向判断
                    if (lightDot1 == 0 && lightDot2 == 0 && lightDot3 == 0 && lightDot4 == 0) {

                        // 存在四个亮点了，判断前后是否有七星阵
                        if (i > 14) {
                            // 既找前面也要找后面
                            // 纵向判断
                            int preDot1 = tempData[i - 1][j];
                            int preDot2 = tempData[i - 2][j];
                            int preDot3 = tempData[i - 3][j];
                            int preDot4 = tempData[i - 4][j];
                            int preDot5 = tempData[i - 5][j];
                            int preDot6 = tempData[i - 6][j];
                            int preDot7 = tempData[i - 7][j];

                            if (isSevenDotRight(preDot1, preDot2, preDot3, preDot4, preDot5, preDot6, preDot7)) {
                                N3 += 40;
                            }
                        }
                        // 只找后面的元素
                        int nextDot1 = tempData[i][j];
                        int nextDot2 = tempData[i + 1][j];
                        int nextDot3 = tempData[i + 2][j];
                        int nextDot4 = tempData[i + 3][j];
                        int nextDot5 = tempData[i + 4][j];
                        int nextDot6 = tempData[i + 5][j];
                        int nextDot7 = tempData[i + 6][j];

                        if (isSevenDotRight(nextDot1, nextDot2, nextDot3, nextDot4, nextDot5, nextDot6, nextDot7)) {
                            N3 += 40;
                        }
                    }

                }


                if(tempData[i][j] == 0){
                    lightNum += 1;
                }else if(tempData[i][j] == 1){
                    darkNum += 1;
                }
            }
        }

        int percentDark = darkNum * 100 / (lightNum + darkNum);
        N4 = Math.abs((percentDark - 50) * 2);

        return N1 + N2 + N3 + N4;
    }


    private boolean isSevenDotRight(int one, int two, int three, int four, int five, int six, int seven) {

        if (one == 1 && two == 0) {
            if (three == 1 && four == 1) {
                if (five == 1 && six == 0 && seven == 1) {
                    return true;
                }
            }
        }

        return false;
    }


    private void reverse(byte[][] dotTable, int i, int j) {

        if (dotTable[i][j] == 0) {
            // 数据位白色
            dotTable[i][j] = 1;
        } else if (dotTable[i][j] == 1) {
            // 数据位黑色
            dotTable[i][j] = 0;
        }

    }
}

package com.doscan.qrcode.standard.table;

import com.doscan.qrcode.standard.qrcode.drawable.ModuleDot;

/**
 * 模点表数据结构(不可以被扩容)
 */
public class DotTable {

    /**
     * 二维数组 数据储存的核心部分
     */
    ModuleDot[][] data;

    /**
     * 模点表构造器
     */
    public DotTable(int sideSize){

        // 初始化数据部分
        data = new ModuleDot[sideSize][sideSize];
        for(int i = 0; i < sideSize ; i++){
            for(int j = 0; j < sideSize ; j++){
                data[i][j] = new ModuleDot();
            }
        }

    }



}

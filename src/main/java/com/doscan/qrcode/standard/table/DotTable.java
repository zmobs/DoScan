package com.doscan.qrcode.standard.table;



/**
 * 模点表数据结构(不可以被扩容)
 */
public class DotTable {



    /**
     * 二维数组 数据储存的核心部分
     */
    byte[][] data;

    public void set(int x,int y,Value value){
        data[x][y] = value.DESOLATION.value;
    }

    /**
     * 模点表构造器
     */
    public DotTable(int sideSize){

        // 初始化数据部分
        data = new byte[sideSize][sideSize];
        for(int i = 0; i < sideSize ; i++){
            for(int j = 0; j < sideSize ; j++){
                data[i][j] = Value.DESOLATION.value;
            }
        }

    }


    public enum Value{

        DARK((byte)1),
        LIGHT((byte)0),
        DESOLATION((byte)-1);

        byte value;
        Value(byte value){
            this.value = value;
        }
    }

}

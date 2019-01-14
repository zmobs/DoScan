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

    public byte[][] getData() {
        return data;
    }

    public Value get(int x, int y){

        if(data[x][y] == 1){
            return Value.DARK;
        }else if(data[x][y] == 0){
            return Value.LIGHT;
        }else{
            return Value.DESOLATION;
        }

    }

    private int sideSize;

    public int getSideSize() {
        return sideSize;
    }

    /**
     * 模点表构造器
     */
    public DotTable(int sideSize){
        this.sideSize = sideSize;
        // 初始化数据部分
        data = new byte[sideSize][sideSize];
        for(int i = 0; i < sideSize ; i++){
            for(int j = 0; j < sideSize ; j++){
                data[i][j] = -1;
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

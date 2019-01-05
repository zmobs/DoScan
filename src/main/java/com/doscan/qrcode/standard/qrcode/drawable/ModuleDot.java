package com.doscan.qrcode.standard.qrcode.drawable;

/**
 * 模点类定义，二维码图形中最基础的组成部分，
 * 标准二维码形式中，只有黑与白两种选项
 */
public class ModuleDot {

    /**
     * 默认模点是荒芜状态
     */
    State state = State.DESOLATE;
    /**
     * 初始化默认颜色，其实这里默认色值不会被使用
     */
    SymbolColor color = SymbolColor.DARK;



    /**
     * 模点状态
     */
    public enum  State{
        /**
         * 活跃状态
         */
        ACTIVE,
        /**
         * 荒芜状态
         */
        DESOLATE
    }

}

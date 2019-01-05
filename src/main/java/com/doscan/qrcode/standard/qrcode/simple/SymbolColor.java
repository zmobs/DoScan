package com.doscan.qrcode.standard.qrcode.simple;

import java.awt.*;

/**
 * 符号的颜色设置
 */
public enum SymbolColor {
    /**
     * 暗色
     */
    DARK(Color.BLACK),
    /**
     * 亮色
     */
    LIGHT(Color.WHITE);

    /**
     * 色值过滤器
     * @param color
     */
    private Color thisColor;

    SymbolColor(Color color){
        this.thisColor = color;
    }

    /**
     * 设置色值颜色
     * @param antherColor
     */
    public void color(Color antherColor){
        this.thisColor = antherColor;
    }

    /**
     * 获取当前设置的色值颜色
     * @return
     */
    public Color getColor(){
        return this.thisColor;
    }

}

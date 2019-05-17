package com.duqingquan.doscan.qrcode.graphics;

import com.duqingquan.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * 图形相关功能 帮助器
 */
public class GraphicsHelper {

    AnimDemoFrame animDemoFrame = new AnimDemoFrame();

    public void showAnimDemo(QRCodeSymbol symbol){
        if(animDemoFrame != null){
            animDemoFrame.setQrCodeSymbol(symbol);
        }else{
            animDemoFrame = new AnimDemoFrame();
        }
    }



}

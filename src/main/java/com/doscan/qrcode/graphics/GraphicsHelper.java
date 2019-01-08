package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class GraphicsHelper {

    public static void showAnim(QRCodeSymbol symbol){
        AnimFrame animFrame = new AnimFrame(symbol);
        animFrame.setVisible(true);
        animFrame.setBounds(80,80,1000,1000);
        animFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

}
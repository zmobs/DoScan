package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;

import java.util.ArrayList;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class GraphicsHelper {

    AnimDemoFrame animDemoFrame = new AnimDemoFrame();

    public void showAnimDemo(QRCodeSymbol symbol){
        if(animDemoFrame != null){
            animDemoFrame.setQrCodeSymbol(symbol);
        }else{
            animDemoFrame = new AnimDemoFrame();
        }
    }

    public void setLogs(ArrayList<String> logs){
        animDemoFrame.setLogs(logs);
    }


    public static void showAnim(QRCodeSymbol symbol){
        AnimFrame animFrame = new AnimFrame(symbol);
        animFrame.setVisible(true);
        animFrame.setBounds(80,80,1000,1000);
        animFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


    public static void showTestAnim(byte[][] data){
        TestAnimFrame animFrame = new TestAnimFrame(data);
        animFrame.setVisible(true);
        animFrame.setBounds(280,180,1000,1000);
        animFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

}

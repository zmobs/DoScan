package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.doscan.qrcode.util.Log;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 动画播放窗体
 */
public class AnimDemoFrame extends JFrame {

    AnimPanel animPanel = new AnimPanel();

    public void setQrCodeSymbol(QRCodeSymbol symbol){
        animPanel.setQrCodeSymbol(symbol);
    }

    public AnimDemoFrame(){
        this.setTitle("QR Code Painter");
        this.add(animPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                animPanel.stop();
            }
        });
        this.setVisible(true);
        this.setBounds(80,80,1000,1000);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



}

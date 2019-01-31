package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.doscan.qrcode.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * 动画播放窗体
 */
public class AnimDemoFrame extends JFrame {

    AnimPanel animPanel = new AnimPanel();
    BCHPanel bchPanel = new BCHPanel();

    public void setQrCodeSymbol(QRCodeSymbol symbol){
        animPanel.setQrCodeSymbol(symbol);
        bchPanel.setQrCodeSymbol(symbol);
    }



    public AnimDemoFrame(){
        this.setTitle("QR Code Painter");
        this.add(bchPanel,BorderLayout.NORTH);
        this.add(animPanel, BorderLayout.CENTER);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                animPanel.stop();
                bchPanel.stop();
            }
        });
        this.setVisible(true);
        this.setBounds(80,80,800,800);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



}

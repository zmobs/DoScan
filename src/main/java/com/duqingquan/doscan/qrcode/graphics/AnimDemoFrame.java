package com.duqingquan.doscan.qrcode.graphics;

import com.duqingquan.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        this.setBounds(380,80,800,800);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



}

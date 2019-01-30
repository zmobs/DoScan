package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AnimDemoFrame extends JFrame {



    public AnimDemoFrame(){
        this.setTitle("QR Code Painter");
        AnimPannel animPannel = new AnimPannel();
        this.add(animPannel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Log.d("rrrrrrr");
                animPannel.stop();
//                super.windowClosed(e);
            }
        });
        this.setVisible(true);
        this.setBounds(80,80,1000,1000);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



}

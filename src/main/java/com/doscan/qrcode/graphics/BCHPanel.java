package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.doscan.qrcode.util.Log;
import com.doscan.qrcode.util.SystemUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BCHPanel extends JScrollPane  {


    QRCodeSymbol qrCodeSymbol;
    DataThread dataThread = new DataThread();


    /**
     * 负责更新数据的界面实现
     */
    class DataThread extends Thread {

        boolean isRun = true;

        public void stopRun() {
            isRun = false;
        }

        @Override
        public void run() {
            for(String log : Log.defaultLog().logs()){
                if(!isRun){
                    break;
                }
                stringBuilder.append(log);
                stringBuilder.append("\n");
                jtextarea.setText(stringBuilder.toString());
                jtextarea.selectAll();
                SystemUtil.sleep(500);
            }
        }

    }

    public void stop() {

        if (dataThread != null) {
            dataThread.stopRun();
            dataThread = null;
        }
    }

    public void setQrCodeSymbol(QRCodeSymbol symbol){
        this.qrCodeSymbol = symbol;
        dataThread.start();
    }


    JTextArea jtextarea = new JTextArea(5,10);
    StringBuilder stringBuilder = new StringBuilder();


    public BCHPanel() {

        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // 设置大小
        stringBuilder.append("数据计算过程:");
        jtextarea.setText(stringBuilder.toString());
        jtextarea.setLineWrap(true);
        jtextarea.setEditable(false);
        jtextarea.setSize(800,300);
        this.setViewportView(jtextarea);


    }
}

package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AnimPannel extends JPanel {

    QRCodeSymbol qrCodeSymbol;
    final int STEP_PIX = 15;
    int PADDING_WIDTH = 120;
    int sideNum = 10;
    byte[][] data = new byte[sideNum][sideNum];
    UIThread uiThread = new UIThread();
    DataThread dataThread = new DataThread();




    class DataThread extends Thread{
        boolean isRun = true;

        public void stopRun(){
            isRun = false;
        }
        @Override
        public void run() {
            super.run();
            while(isRun){
                int mI = 0;
                int mJ = 0;
                for(int i = 0; i < sideNum;i++){
                    for(int j = 0; j < sideNum;j++){
                        int peerValue = data[i][j];
                        if(peerValue == -1){
                            mI = i ;
                            mJ = j;
                            data[i][j] = (byte)((i + j) % 2 == 0 ? 1 : 0);
                            break;
                        }else {
                            continue;
                        }
                    }
                    break;
                }
                Log.d(" --------  " + mI + "  jjjjj---  " + mJ);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    class UIThread extends Thread{
        boolean isRun = true;

        public void stopRun(){
            isRun = false;
        }

        @Override
        public void run() {
            super.run();

            AnimPannel.this.validate();
            AnimPannel.this.repaint();

            while(isRun){
                // 100 ms 一帧
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop(){
        if(uiThread != null){
            uiThread.stopRun();
            uiThread = null;
        }
        if(dataThread != null){
            dataThread.stopRun();
            dataThread = null;
        }
    }

    public AnimPannel() {

        PADDING_WIDTH = 5 * STEP_PIX;

        for(int i = 0; i < sideNum;i++) {
            for (int j = 0; j < sideNum; j++) {
                data[i][j] = -1;
            }
        }

        // 新启一个线程，不断的刷新界面
        uiThread.start();
        dataThread.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.blue);
        g.fillRect(0,0,1000,1000);


        // 递归绘制所有的模点，并将其绘制

        int sideNum = data.length;
        for(int i = 0; i < sideNum;i++){
            for(int j = 0; j < sideNum;j++){
                byte perValue = data[i][j];

                int xPos = PADDING_WIDTH + i * STEP_PIX;
                int yPos = PADDING_WIDTH + j * STEP_PIX;
                if(perValue == 0){
                    g.setColor(Color.WHITE);
                }else if(perValue == 1){
                    g.setColor(Color.BLACK);
                }else if(perValue == -2){
                    g.setColor(Color.BLUE);
                }else {
                    g.setColor(Color.RED);
                }
                g.fillRect(xPos,yPos,STEP_PIX,STEP_PIX);
            }
        }
    }
}

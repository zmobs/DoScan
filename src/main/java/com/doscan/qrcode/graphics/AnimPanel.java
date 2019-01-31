package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.ReentrantLock;


public class AnimPanel extends JPanel {

    QRCodeSymbol qrCodeSymbol;

    final int STEP_PIX = 15;
    int PADDING_WIDTH = 120;
    byte[][] data = new byte[0][0];
    UIThread uiThread = new UIThread();
    DataThread dataThread = new DataThread();
    ReentrantLock dataLock = new ReentrantLock();

    public void setQrCodeSymbol(QRCodeSymbol symbol){
        this.qrCodeSymbol = symbol;
        dataThread.start();
    }

    public AnimPanel() {

        PADDING_WIDTH = 5 * STEP_PIX;

        // 新启一个线程，不断的刷新界面
        uiThread.start();

    }

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
            qrCodeSymbol.plateWithAnim();
        }

    }

    /**
     * 负责更新UI的线程实现
     */
    class UIThread extends Thread {

        boolean isRun = true;

        public void stopRun() {
            isRun = false;
        }

        @Override
        public void run() {

            while (isRun) {

                // 200 ms 一帧+
                AnimPanel.this.validate();
                AnimPanel.this.repaint();

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        if (uiThread != null) {
            uiThread.stopRun();
            uiThread = null;
        }
        if (dataThread != null) {
            dataThread.stopRun();
            dataThread = null;
        }
    }



    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (dataLock.tryLock()) {

            data = qrCodeSymbol.obtainAnimData();
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, 1000, 1000);

            // 递归绘制所有的模点，并将其绘制
            int sideNum = data.length;
            for (int i = 0; i < sideNum; i++) {
                for (int j = 0; j < sideNum; j++) {
                    byte perValue = data[i][j];

                    int xPos = PADDING_WIDTH + i * STEP_PIX;
                    int yPos = PADDING_WIDTH + j * STEP_PIX;
                    if (perValue == 0) {
                        g.setColor(Color.WHITE);
                    } else if (perValue == 1) {
                        g.setColor(Color.BLACK);
                    } else if (perValue == -2) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.fillRect(xPos, yPos, STEP_PIX, STEP_PIX);
                }
            }
            dataLock.unlock();
        }

    }
}

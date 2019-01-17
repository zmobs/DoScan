package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.doscan.qrcode.standard.table.DotTable;

import javax.swing.*;
import java.awt.*;

public class TestAnimFrame extends JFrame {


    byte[][] qrCodeSymbol;
    final int STEP_PIX = 15;
    int PADDING_WIDTH = 120;

    public TestAnimFrame(byte[][] symbol){
        int dataSize =  symbol.length;
        qrCodeSymbol = new byte[dataSize][dataSize];
        for(int i = 0; i < dataSize; i++){
            for(int j = 0; j < dataSize; j++){
                qrCodeSymbol[i][j] = symbol[i][j];
            }
        }
        this.setTitle("222222222222 test");
        PADDING_WIDTH = 5 * STEP_PIX;
    }


    @Override
    public void paint(Graphics g) {

        // 递归绘制所有的模点，并将其绘制
        byte[][] data = qrCodeSymbol;
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

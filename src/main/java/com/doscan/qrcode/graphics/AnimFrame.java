package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.util.Log;

import javax.swing.*;
import java.awt.*;

public class AnimFrame extends JFrame {


    QRCodeSymbol qrCodeSymbol;
    final int STEP_PIX = 15;
    int PADDING_WIDTH = 120;

    public AnimFrame(QRCodeSymbol symbol){
        qrCodeSymbol = symbol;
        this.setTitle("QR Code Painter");
        PADDING_WIDTH = symbol.QUIET_ZONE_SIZE * STEP_PIX;
    }

    @Override
    public void paint(Graphics g) {

        // 递归绘制所有的模点，并将其绘制
        DotTable dotTable = qrCodeSymbol.getDotTable();
        byte[][] data = dotTable.getData();
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

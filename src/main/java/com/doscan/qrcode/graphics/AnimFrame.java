package com.doscan.qrcode.graphics;

import com.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.util.Log;

import javax.swing.*;
import java.awt.*;

public class AnimFrame extends JFrame {


    QRCodeSymbol qrCodeSymbol;
    final int STEP_PIX = 15;
    final int PADDING_WIDTH = 120;
    public AnimFrame(QRCodeSymbol symbol){
        qrCodeSymbol = symbol;
        this.setTitle("QR Code Painter");
//        Container conn = jf.getContentPane();
    }



    @Override
    public void paint(Graphics g) {
//        super.paint(g);
//        g.setColor(Color.RED);
//        g.fillRect(200,200,STEP_PIX,STEP_PIX);
//        g.setColor(Color.BLUE);
//        g.fillRect(100,100,STEP_PIX,STEP_PIX);
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
                    Log.d("perValue  ----  " + perValue );
                    g.setColor(Color.WHITE);
                }else if(perValue == 1){
                    Log.d("perValue  ----  " + perValue );
                    g.setColor(Color.BLACK);
                }else{
                    g.setColor(Color.RED);
                }
                g.fillRect(xPos,yPos,STEP_PIX,STEP_PIX);
            }
        }

    }
}

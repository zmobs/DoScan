package com.duqingquan.doscan.qrcode.output;

import com.duqingquan.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageOutputer implements IOutputer{

    private String pngFilePath;
    final int STEP_PIX = 15;
    int PADDING_WIDTH = 120;


    public ImageOutputer(String pngFile){
        this.pngFilePath = pngFile;
    }

    @Override
    public boolean output(QRCodeSymbol symbol) {


        BufferedImage bufferedImage = new BufferedImage(1000,1000,TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        byte[][] data = symbol.getDotTable().getData();
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


        try {
            ImageIO.write(bufferedImage, "PNG", new File(pngFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

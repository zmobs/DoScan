package com.duqingquan.doscan.qrcode.output;

import com.duqingquan.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;

public class PngOutputer implements IOutputer{

    private String pngFilePath;

    PngOutputer(String pngFile){
        this.pngFilePath = pngFile;
    }

    @Override
    public boolean output(QRCodeSymbol symbol) {
        return false;
    }
}

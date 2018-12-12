package com.doscan.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class Main {

    public static void main(String args[]){

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            qrCodeWriter.encode("996abc", BarcodeFormat.QR_CODE,300,300);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        QREncoder
                .obain()
                .content("996abc")
                .code();
    }

}

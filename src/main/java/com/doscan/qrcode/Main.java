package com.doscan.qrcode;

import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;


public class Main {


    public static void main(String args[]){


//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        try {
//            qrCodeWriter.encode("996688", BarcodeFormat.QR_CODE,300,300);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }

        QREncoder
                .obain()
                .version(2)
                .correctLevel(ErrorCorrectLevel.H)
                .content("110120119")
                .code();

    }

}

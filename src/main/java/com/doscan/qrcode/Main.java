package com.doscan.qrcode;

import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.standard.charset.Charset;
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
                // 手动指定容量版本和纠错级别
//                .version(2)
//                .correctLevel(ErrorCorrectLevel.M)
                .strategy(EncodeStrategy.HIGN_QUALITY)
                .charset(Charset.ISO_8859_1)
                .content("11012011100")
                .code();

    }

}

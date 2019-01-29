package com.doscan.qrcode;

import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.standard.charset.Charset;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MatrixToImageWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Main {


    public static void main(String args[]){


        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            Map<EncodeHintType,Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "GB2312");
            BitMatrix bitMatrix =  qrCodeWriter.encode("己亥猪年2019",
                    BarcodeFormat.QR_CODE,300,300,hints);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        QREncoder
                .obain()
                // 手动指定容量版本和纠错级别
//                .version(2)
//                .correctLevel(ErrorCorrectLevel.M)
                .strategy(EncodeStrategy.HIGN_QUALITY)
                .charset(Charset.GB2312)
                .content("己亥猪年2019")
                .setAnimDemo(true)
                .code();

    }

}

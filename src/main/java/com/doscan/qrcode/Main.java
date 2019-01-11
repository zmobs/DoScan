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
            hints.put(EncodeHintType.QR_VERSION, 2);
            BitMatrix bitMatrix =  qrCodeWriter.encode("11012011100111222222222", BarcodeFormat.QR_CODE,300,300,hints);
//            //直接写入文件
//            File outputFile = new File("d:/new.png");
//            MatrixToImageWriter.writeToFile(bitMatrix, "png", outputFile);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        QREncoder
                .obain()
                // 手动指定容量版本和纠错级别
//                .version(2)
//                .correctLevel(ErrorCorrectLevel.M)
                .strategy(EncodeStrategy.HIGN_QUALITY)
                .charset(Charset.ISO_8859_1)
                .content("1101201110011122222222211012011100111222222222" +
                        "110120111001112222222221101201110011122222222211012011100111222222222" +
                        "110120111001112222222221101201110011122222222211012011100111222222222")
                .code();

    }

}

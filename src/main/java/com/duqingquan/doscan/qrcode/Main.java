package com.duqingquan.doscan.qrcode;

import com.duqingquan.doscan.qrcode.proto.EncodeStrategy;
import com.duqingquan.doscan.qrcode.standard.charset.Charset;


public class Main {


    public static void main(String args[]){


//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        try {
//            Map<EncodeHintType,Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            hints.put(EncodeHintType.CHARACTER_SET, "GB2312");
//            BitMatrix bitMatrix =  qrCodeWriter.encode("己亥猪年2019",
//                    BarcodeFormat.QR_CODE,300,300,hints);
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }

        QREncoder
                .obain()
                // 手动指定容量版本和纠错级别
//                .version(2)
//                .correctLevel(ErrorCorrectLevel.M)
                .strategy(EncodeStrategy.HIGN_QUALITY)
                .charset(Charset.GB2312)
                .content("2019 文体两开花，加油~")
                .setAnimDemo(true)
                .code();

    }

}

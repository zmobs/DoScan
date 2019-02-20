package com.duqingquan.doscan.qrcode;

import com.duqingquan.doscan.qrcode.proto.EncodeStrategy;
import com.duqingquan.doscan.qrcode.standard.charset.Charset;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class Main {


    public static void main(String args[]){


//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        try {
//            Map<EncodeHintType,Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            hints.put(EncodeHintType.CHARACTER_SET, "GB2312");
//            BitMatrix bitMatrix =  qrCodeWriter.encode("己亥猪年2019",
//                    BarcodeFormat.QR_CODE,300,300,hints);
//            MatrixToImageWriter.writeToFile(bitMatrix,"jpg",new
//                    File("d://2.jpg"));
//        } catch (WriterException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        QRCodeReader qrCodeReader = new QRCodeReader();
//        String filePath = "d://2.jpg"; //new.png
//        File file = new File(filePath);
//
//        try {
//            BufferedImage image = ImageIO.read(file);
//            int w = image.getWidth();
//            int h = image.getHeight();
////            int[] imageData = new int[w * h * 3];
////            image.getData().getPixel(0,0,imageData);
//
//            int[] pixels = new int[w * h];
//            int iw = image.getWidth();
//            int ih = image.getHeight();
//            int i = 0;
//            for (int y = 0; y < ih; y++) {
//                for (int x = 0; x < iw; x++) {
//                    int pixel = image.getRGB(x, y);
////                    int alpha = (pixel >> 24) & 0xFF;
////                    int red = (pixel >> 16) & 0xFF;
////                    int green = (pixel >> 8) & 0xFF;
////                    int blue = pixel & 0xFF;
//                    pixels[i] = pixel;
//                    i++;
//                }
//            }
////            bitmap.getPixels(pixels,0,width,0,0,width,height);
//
//
//            LuminanceSource source = new RGBLuminanceSource(w,h,pixels);
//            Binarizer  binarizer = new HybridBinarizer(source);
//            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
//
//            Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
//            hints.put(DecodeHintType.CHARACTER_SET, "GB2312");
//
//            Result result = qrCodeReader.decode(binaryBitmap,hints);
//
//            System.out.println("result = "+ result.toString());
//            System.out.println("resultFormat = "+ result.getBarcodeFormat());
//            System.out.println("resultText = "+ result.getText());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (FormatException e) {
//            e.printStackTrace();
//        } catch (ChecksumException e) {
//            e.printStackTrace();
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//        }

//        QREncoder
//                .obain()
//                // 手动指定容量版本和纠错级别
////                .version(2)
////                .correctLevel(ErrorCorrectLevel.M)
//                .strategy(EncodeStrategy.HIGN_QUALITY)
//                .charset(Charset.GB2312)
//                .content("2019 文体两开花，加油~")
//                .setAnimDemo(true)
//                .code();

    }

}

//package com.doscan.qrcode.standard.qrcode;
//
//import Log;
//
///**
// * 双字节中文文本模式(也是对比zxing 优化的重点)
// */
//public class ChineseInputThing extends InputThing{
//
//    @Override
//    public Byte[] getModeIndicator() {
//        return new Byte[0];
//    }
//
//    @Override
//    public boolean isMatch(String content) {
//        Log.d("content getBytes---- " + content.getBytes().length);
//        Log.d("content length---- " + content.length());
//        return false;
//    }
//
//    @Override
//    public Byte[] getBits(String content) {
//        return new Byte[0];
//    }
//
//
//
//    @Override
//    public String getName() {
//        return "shift jis编码模式";
//    }
//}

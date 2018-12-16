//package com.doscan.qrcode.standard.qrcode;
//
///**
// * 双字节日本字文本模式
// */
//public class ShiftJISInputThing extends InputThing{
//
//    @Override
//    public Byte[] getModeIndicator() {
//        Byte[] indicator = {1,0,0,0};
//        return indicator;
//    }
//
//    @Override
//    public boolean isMatch(String content) {
//        return false;
//    }
//
//    @Override
//    public Byte[] getBits(String content) {
//        return new Byte[0];
//    }
//
//    @Override
//    public String getName() {
//        return "shift jis编码模式";
//    }
//}

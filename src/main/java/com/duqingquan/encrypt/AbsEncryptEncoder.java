//package com.duqingquan.encrypt;
//
//import com.duqingquan.doscan.qrcode.util.Log;
//
//import java.io.UnsupportedEncodingException;
//
//public abstract class AbsEncryptEncoder {
//
//    protected byte[] srcInfo;
//    protected byte[] finalInfo;
//    protected int key = 0;
//
//    boolean isEncrypt = true;
//    protected final byte EncryptedFlag = (byte)200;
//
//    public AbsEncryptEncoder source(byte[] src){
//        srcInfo = src;
//        return this;
//    }
//
//    public AbsEncryptEncoder key(int key){
//        this.key = key;
//
//        return this;
//    }
//
//    private boolean isPrepared(boolean isEncrypt){
//
//        if(isEncrypt){
//            if(srcInfo == null){
//                return false;
//            }
//        }else{
//            if(finalInfo == null){
//                return false;
//            }
//        }
//
//        if(key == 0){
//            return false;
//        }
//
//        return true;
//    }
//
//    public AbsEncryptEncoder encrypt(){
//        if(!isPrepared(true)){
//            Log.bomb("加密组件初始化失败");
//        }
//        finalInfo = doEncrypt();
//        isEncrypt = true;
//        return this;
//    }
//
//    public AbsEncryptEncoder decrypt(){
//        if(!isPrepared(false)){
//            Log.bomb("解密组件初始化失败");
//        }
//        srcInfo = doDecrypt();
//        isEncrypt = false;
//        return this;
//    }
//
//    public byte[] finalBytes(){
//        return this.finalInfo;
//    }
//
//
//    public String string(){
//        return string(null);
//    }
//
//    public String string(String charset){
//        if(finalInfo == null){
//            Log.bomb("final info null");
//        }
//        if(charset == null){
//            if(isEncrypt){
//                return new String(finalInfo);
//            }else{
//                return new String(srcInfo);
//            }
//
//        }else{
//            try {
//                if(isEncrypt){
//                    return new String(finalInfo,charset);
//                }else{
//                    return new String(srcInfo,charset);
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                Log.bomb("UnsupportedEncodingException");
//                return null;
//            }
//        }
//    }
//
//    public abstract byte[] doEncrypt();
//
//    public abstract byte[] doDecrypt();
//
//}

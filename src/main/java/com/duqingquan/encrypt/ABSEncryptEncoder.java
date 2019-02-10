package com.duqingquan.encrypt;

import com.duqingquan.doscan.qrcode.util.Log;

import java.io.UnsupportedEncodingException;

public abstract class ABSEncryptEncoder {

    protected byte[] srcInfo;
    protected byte[] finalInfo;
    protected int key = 0;

    protected final byte EncryptedFlag = (byte)200;

    public ABSEncryptEncoder source(byte[] src){
        srcInfo = src;
        return this;
    }

    public ABSEncryptEncoder key(int key){
        this.key = key;

        return this;
    }

    private boolean isPrepared(){

        if(srcInfo == null){
            return false;
        }
        if(key == 0){
            return false;
        }

        return true;
    }

    public ABSEncryptEncoder encrypt(){
        if(!isPrepared()){
            Log.bomb("加密组件初始化失败");
        }
        finalInfo = doEncrypt();
        return this;
    }

    public String string(){
        return string(null);
    }

    public String string(String charset){
        if(finalInfo == null){
            Log.bomb("final info null");
        }
        if(charset == null){
            return new String(finalInfo);
        }else{
            try {
                return new String(finalInfo,charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.bomb("UnsupportedEncodingException");
                return null;
            }
        }
    }

    public abstract byte[] doEncrypt();

}

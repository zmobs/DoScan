package com.doscan.qrcode;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.proto.QRCode;
import com.doscan.qrcode.standard.charset.Charset;
import com.doscan.qrcode.standard.qrcode.InputThing;
import com.doscan.qrcode.standard.qrcode.Version;
import com.doscan.qrcode.util.LogUtil;
import com.doscan.qrcode.util.StringUtil;


public class QREncoder {

    private IQRCode2015 type;
    private Version version;
    private Charset charset = Charset.ISO_8859_1;

    public static QREncoder obain(){
        return new QREncoder();
    }


    private String content;

    public QREncoder content(String input){
        this.content = input;
        return this;
    }

    public QREncoder charset(Charset charset){

        if(charset == null){
            throw new BombException("字符集不能为空");
        }
        if(! java.nio.charset.Charset.isSupported(charset.content())){
            throw new BombException("系统字符集不支持");
        }

        this.charset = charset;
        return this;
    }




    public QRCode code(){

        InputResolver inputResolver = new InputResolver();
        InputThing inputThing = inputResolver.detect(content);
        LogUtil.log("inputThing ---- " + inputThing.getName());


        QRCode qrCode = new QRCode();
        return qrCode;
    }

}

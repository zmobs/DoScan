package com.doscan.qrcode;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.proto.QRCode;
import com.doscan.qrcode.standard.qrcode.InputThing;
import com.doscan.qrcode.standard.qrcode.Version;
import com.doscan.qrcode.util.LogUtil;
import com.doscan.qrcode.util.StringUtil;

import java.nio.charset.Charset;

public class QREncoder {

    private IQRCode2015 type;
    private Version version;
    private String charset = "ISO-8859-1";

    public static QREncoder obain(){
        return new QREncoder();
    }


    private String content;

    public QREncoder content(String input){
        this.content = input;
        return this;
    }

    public QREncoder charset(String charset){
        if(StringUtil.isEmpty(charset)){
            return this;
        }
        if(!Charset.isSupported(charset)){
            throw new BombException("系统字符集不支持");
        }
        this.charset = charset;
        return this;
    }



    public QRCode code(){

        InputResolver inputResolver = new InputResolver();
        InputThing inputThing = inputResolver.detect(content);
        LogUtil.log("inputThing ---- " + inputThing.getName());
        inputThing.getBits(content);


        QRCode qrCode = new QRCode();
        return qrCode;
    }

}

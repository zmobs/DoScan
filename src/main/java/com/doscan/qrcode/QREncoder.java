package com.doscan.qrcode;

import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.proto.QRCode;
import com.doscan.qrcode.standard.qrcode.InputThing;
import com.doscan.qrcode.standard.qrcode.Version;
import com.doscan.qrcode.util.LogUtil;

public class QREncoder {

    private IQRCode2015 type;
    private Version version;


    public static QREncoder obain(){
        return new QREncoder();
    }


    private String content;

    public QREncoder content(String input){
        this.content = input;
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

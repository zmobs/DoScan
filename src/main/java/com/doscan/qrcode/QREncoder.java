package com.doscan.qrcode;

import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.proto.QRCode;
import com.doscan.qrcode.standard.qrcode.Version;

public class QREncoder {

    private IQRCode2015 type;
    private Version version;


    public static QREncoder obain(){
        return new QREncoder();
    }


    private String content;

    public QREncoder content(String input){
        this.content = input;
    }



    public QRCode code(){

        InputResolver inputResolver = new InputResolver();
        inputResolver.

        QRCode qrCode = new QRCode();
        return qrCode;
    }

}

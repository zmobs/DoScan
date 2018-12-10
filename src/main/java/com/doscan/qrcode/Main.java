package com.doscan.qrcode;

public class Main {

    public static void main(String args[]){

        QREncoder
                .obain()
                .content("hello,aaa")
                .code();
    }

}

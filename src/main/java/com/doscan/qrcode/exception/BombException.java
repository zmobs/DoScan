package com.doscan.qrcode.exception;

public class BombException extends RuntimeException {

    /**
     * 描述信息
     */
    private String desc;

    public BombException(String desc){
        this.desc = desc;
    }

}

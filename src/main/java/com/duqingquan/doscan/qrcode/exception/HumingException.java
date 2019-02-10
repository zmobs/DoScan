package com.duqingquan.doscan.qrcode.exception;

public class HumingException extends Exception {

    /**
     * 描述信息
     */
    private String desc;

    public HumingException(String desc){
        this.desc = desc;
    }

}

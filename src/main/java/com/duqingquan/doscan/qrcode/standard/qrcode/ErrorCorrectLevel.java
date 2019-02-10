package com.duqingquan.doscan.qrcode.standard.qrcode;

/**
 * @document IOS-18004 2016 6.1
 * 纠错码级别
 */
public enum  ErrorCorrectLevel {

    /**
     * 7%
     */
    L("01"),
    /**
     * 15%
     */
    M("00"),
    /**
     * 25%
     */
    Q("11"),
    /**
     * 30%
     */
    H("10");


    /**
     * 各个级别的标示位
     */
    private final String flag;

    ErrorCorrectLevel(String str){
        this.flag = str;
    }

    public String getValue(){
        return flag;
    }
}

package com.doscan.qrcode.standard;

/**
 * @document IOS-18004 2016 6.1
 * 纠错码级别
 */
public enum  ErrorCorrectLevel {

    /**
     * 7%
     */
    L(7),
    /**
     * 15%
     */
    M(15),
    /**
     * 25%
     */
    Q(25),
    /**
     * 30%
     */
    H(30);


    /**
     * 各个级别的标示位
     */
    private final int flag;

    ErrorCorrectLevel(int flag){
        this.flag = flag;
    }

    public int getValue(){
        return flag;
    }
}

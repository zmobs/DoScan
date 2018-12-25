package com.doscan.qrcode.standard.qrcode.errorcorrect;

/**
 * 纠错码块，二维码版本数据的基本组成部分
 */
public class ECBlock {

    // 整个ECB块的码字长度
    int count;
    //
    int dataCodeWordNum;
    /**
     * 纠错码字数量
     */
    int ecCodeWordNum;
}

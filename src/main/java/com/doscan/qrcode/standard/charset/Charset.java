package com.doscan.qrcode.standard.charset;

public enum  Charset {


    ISO_8859_1("ISO-8859-1"),
    GB2312("GB2312"),
    GBK("GBK"),
    UTF8("UTF-8"),
    JIS("JIS");


    private String tag;

    Charset(String charsetStr){
        this.tag = charsetStr;
    }

    public String content(){
        return tag;
    }
}

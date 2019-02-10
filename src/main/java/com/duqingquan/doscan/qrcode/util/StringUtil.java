package com.duqingquan.doscan.qrcode.util;

public class StringUtil {


    public static boolean isEmpty(String text){

        if(text == null){
            return true;
        }
        if("".equals(text)){
            return true;
        }
        return false;
    }

}

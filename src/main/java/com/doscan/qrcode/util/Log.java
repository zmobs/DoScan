package com.doscan.qrcode.util;


import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.exception.HumingException;

public class Log {


    public static void d(String log){
        System.out.println(log);
    }


    public static void bomb(String desc){
        throw new BombException(desc);
    }


    public static void huming(String desc) throws HumingException{
        throw new HumingException(desc);
    }

}

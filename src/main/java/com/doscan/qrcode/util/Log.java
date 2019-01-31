package com.doscan.qrcode.util;


import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.exception.HumingException;

import java.util.ArrayList;

public class Log {

    ArrayList<String> bchLogs = new ArrayList<>();
    static Log defaultLog = new Log();

    public void record(String log){
        bchLogs.add(log);
    }

    public ArrayList<String> logs(){
        return bchLogs;
    }

    public static Log defaultLog(){
        return defaultLog;
    }


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

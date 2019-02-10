package com.duqingquan.doscan.qrcode.util;

public class SystemUtil {

    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

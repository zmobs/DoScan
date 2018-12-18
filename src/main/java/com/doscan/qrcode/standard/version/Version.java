package com.doscan.qrcode.standard.version;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.util.LogUtil;

/**
 * qrcode的版本对象
 * iso-18004-2015 table1
 */
public class Version {

    /**
     * 1-40
     */
    private int versionNumber;

    public Version(int num){
        this.versionNumber = num;
    }


    public int getSideModuleNum(){
        return 21 + (versionNumber - 1) * 4;
    }

    public int getFunctionPatternNum(){

        if(versionNumber == 1){
            return 202;
        }else if(versionNumber == 2){
            return 235;
        }else if(versionNumber < 7){
            return 235 + (versionNumber - 2) * 8;
        }else if(versionNumber == 7){
            return 390;
        }else if(versionNumber < 14){
            return 390 + (versionNumber - 7) * 8;
        }else if(versionNumber == 14){
            return 611;
        }else if(versionNumber < 21){
            return 611 + (versionNumber - 14) * 8;
        }else if(versionNumber == 21){
            return 882;
        }else if(versionNumber < 28){
            return 882 + (versionNumber - 21) * 8;
        }else if(versionNumber  == 28){
            return 1203;
        }else if(versionNumber < 35){
            return 1203 + (versionNumber - 28) * 8;
        }else if(versionNumber == 35){
            return 1574;
        }else if(versionNumber < 41){
            return 1574 + (versionNumber - 35) * 8;
        }

        throw new BombException("version num 0-40");
    }


    public int getVersionNumber(){
        return this.versionNumber;
    }

    public int getFormatVersionNumber(){

        if(versionNumber < 7){
            return 31;
        }else{
            return 67;
        }
    }


    public int getDataModuleExceptNum(){
        int a = getSideModuleNum();
        int b = getFunctionPatternNum();
        int c = getFormatVersionNumber();

//        LogUtil.log("getSideModuleNum  ---- " + a);
//        LogUtil.log("getFunctionPatternNum  ---- " + b);
//        LogUtil.log("getFormatVersionNumber  ---- " + c);

        int d = a * a - b  - c;
        return d;
    }


    /**
     * codeword
     * @return
     */
    public int getDataCapacity(){
        int a = getDataModuleExceptNum();
        int b = getSideModuleNum();
        int c = a / b;
        return c;
    }

    /**
     * 获取剩余的留白点数
     * @return
     */
    public int getRemainder(){

        int a = getDataModuleExceptNum();
        int c = a % 8;
        return c;
    }
}

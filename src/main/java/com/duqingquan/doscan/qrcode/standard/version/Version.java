package com.duqingquan.doscan.qrcode.standard.version;

import com.duqingquan.doscan.qrcode.exception.BombException;
import com.duqingquan.doscan.qrcode.standard.qrcode.errorcorrect.ECBlock;
import com.duqingquan.doscan.qrcode.standard.qrcode.input.*;

/**
 * qrcode的版本对象
 * iso-18004-2015 table1
 */
public class Version {

    /**
     * 1-40
     */
    private int versionNumber;

    private ECBlock[] ecBlocks;

    /**
     * 获取版本的数字形态
     * @return
     */
    public int getVerNum(){
        return versionNumber;
    }

    /**
     * 构造器
     * @param num
     */
    public Version(int num){
        this.versionNumber = num;
        // todo 根据版本号去初始化 ec块结构
    }


    /**
     * 单边模点数量
     * @return
     */
    public int getSideModuleNum(){
        return 21 + (versionNumber - 1) * 4;
    }

    /**
     * 字符长度指示器比特长度
     * @param inputThing
     * @return
     */
    public int getCharIndicatorCount(InputThing inputThing){

        if(inputThing instanceof NumberInputThing){
            if(versionNumber < 10){
                return 10;
            }else if(versionNumber < 27){
                return 12;
            }else{
                return 14;
            }
        }else if(inputThing instanceof AlphanumericInputThing){
            if(versionNumber < 10){
                return 9;
            }else if(versionNumber < 27){
                return 11;
            }else{
                return 13;
            }
        }else if(inputThing instanceof ByteInputThing){
            if(versionNumber < 10){
                return 8;
            }else if(versionNumber < 27){
                return 16;
            }else{
                return 16;
            }
        }else if(inputThing instanceof ShiftJISInputThing){
            // kanji mode
            if(versionNumber < 10){
                return 8;
            }else if(versionNumber < 27){
                return 10;
            }else{
                return 12;
            }
        }
        throw new BombException("no thus mode .. bomb~~~");
    }

    /**
     * 获取功能模点数量
     * @return
     */
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

    /**
     * 获取版本号
     * @return
     */
    public int getVersionNumber(){
        return this.versionNumber;
    }

    /**
     * 获取格式信息的模点数量
     * @return
     */
    public int getFormatVersionNumber(){

        if(versionNumber < 7){
            return 31;
        }else{
            return 67;
        }
    }


    /**
     * 获取期望的数据段模点数量
     * @return
     */
    public int getDataModuleExceptNum(){
        int a = getSideModuleNum();
        int b = getFunctionPatternNum();
        int c = getFormatVersionNumber();
        int d = a * a - b  - c;
        return d;
    }


    /**
     * codeword 获取数据容量的码字
     * @return
     */
    public int getDataCapacityCodeword(){
        int a = getDataModuleExceptNum();
        int b = 8;
        int c = a / b;
        return c;
    }

    /**
     * 获取剩余的留白比特数量
     * @return
     */
    public int getRemainder(){
        int a = getDataModuleExceptNum();
        int c = a % 8;
        return c;
    }


}

package com.duqingquan.doscan.qrcode.standard.qrcode.input;

import com.duqingquan.doscan.qrcode.proto.BitArray;
import com.duqingquan.doscan.qrcode.standard.version.Version;

/**
 * 输入的事物
 */
public abstract class InputThing {

    protected String content;

    InputThing(String input){
        this.content = input;
    }

    InputThing(){

    }

    public void content(String input){
        this.content = input;
    }


    public String getStrContent(){
        return this.content;
    }

    public abstract BitArray getModeIndicator();

    public abstract int getCapNum(int bitNum);

    public abstract boolean isMatch();

    public abstract BitArray getBits();

    public abstract String getName();

    public abstract BitArray getCountLength(Version version);

}

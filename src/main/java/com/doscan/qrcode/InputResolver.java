package com.doscan.qrcode;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.standard.qrcode.*;
import com.doscan.qrcode.util.LogUtil;
import com.doscan.qrcode.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class InputResolver {

    private List<InputThing> inputThings = new ArrayList<>(8);

    public InputResolver(){
        inputThings.add(new NumberInputThing());
        inputThings.add(new ShiftJISInputThing());
        inputThings.add(new MixInputThing());
        inputThings.add(new ByteInputThing());
        inputThings.add(new ChineseInputThing());
        inputThings.add(new AlphanumericInputThing());
    }



    public InputThing detect(String content){

        if(StringUtil.isEmpty(content)){
            throw new BombException("输入内容为空");
        }

        for(InputThing inputThing : inputThings){
            if(inputThing.isMatch(content)){
                return inputThing;
            }
        }

        LogUtil.log("no input match.  bomb!");

        return null;
    }
}

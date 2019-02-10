package com.duqingquan.doscan.qrcode;

import com.duqingquan.doscan.qrcode.exception.BombException;
import com.duqingquan.doscan.qrcode.standard.charset.Charset;
import com.duqingquan.doscan.qrcode.standard.qrcode.input.AlphanumericInputThing;
import com.duqingquan.doscan.qrcode.standard.qrcode.input.ByteInputThing;
import com.duqingquan.doscan.qrcode.standard.qrcode.input.InputThing;
import com.duqingquan.doscan.qrcode.standard.qrcode.input.NumberInputThing;
import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class InputResolver {

    private List<InputThing> inputThings = new ArrayList<>(8);

    public InputResolver() {
        inputThings.add(new NumberInputThing());
        inputThings.add(new AlphanumericInputThing());
        //inputThings.add(new ShiftJISInputThing());
        //inputThings.add(new MixInputThing());
        //inputThings.add(new ChineseInputThing());
    }


    public InputThing detect(String content, Charset charset) {
        return detect(content,charset,false);
    }


    public InputThing detect(String content, Charset charset,boolean isRecord) {

        if (StringUtil.isEmpty(content)) {
            throw new BombException("输入内容为空");
        }

        for (InputThing inputThing : inputThings) {
            inputThing.content(content);
            if (inputThing.isMatch()) {
                if(isRecord){
                    Log.defaultLog().record("输入模式匹配 ：" + inputThing.getName());
                }
                return inputThing;
            }else{
                if(isRecord){
                    Log.defaultLog().record("输入模式不匹配 pass ：" + inputThing.getName());
                }
            }
        }

        // 兜底的是字节模式
        ByteInputThing byteInputThing = new ByteInputThing(charset);
        byteInputThing.content(content);
        if(isRecord){
            Log.defaultLog().record("兜底输入模式 ：" + byteInputThing.getName());
        }

        return byteInputThing;
    }
}

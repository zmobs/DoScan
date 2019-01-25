package com.doscan.qrcode;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.standard.charset.Charset;
import com.doscan.qrcode.standard.qrcode.input.AlphanumericInputThing;
import com.doscan.qrcode.standard.qrcode.input.ByteInputThing;
import com.doscan.qrcode.standard.qrcode.input.InputThing;
import com.doscan.qrcode.standard.qrcode.input.NumberInputThing;
import com.doscan.qrcode.util.Log;
import com.doscan.qrcode.util.StringUtil;

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

        if (StringUtil.isEmpty(content)) {
            throw new BombException("输入内容为空");
        }

        for (InputThing inputThing : inputThings) {
            inputThing.content(content);
            if (inputThing.isMatch()) {
                return inputThing;
            }
        }

        // 兜底的是字节模式
        ByteInputThing byteInputThing = new ByteInputThing(charset);
        byteInputThing.content(content);

        return byteInputThing;
    }
}

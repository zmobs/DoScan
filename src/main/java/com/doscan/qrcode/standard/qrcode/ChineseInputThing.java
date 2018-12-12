package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.util.LogUtil;

/**
 * 双字节中文文本模式(也是对比zxing 优化的重点)
 */
public class ChineseInputThing extends InputThing{

    @Override
    public Byte[] getModeIndicator() {
        return new Byte[0];
    }

    @Override
    public boolean isMatch(String content) {
        LogUtil.log("content getBytes---- " + content.getBytes().length);
        LogUtil.log("content length---- " + content.length());
        return false;
    }

    @Override
    public Byte[] getBits() {
        return new Byte[0];
    }

    @Override
    public String getName() {
        return "shift jis编码模式";
    }
}

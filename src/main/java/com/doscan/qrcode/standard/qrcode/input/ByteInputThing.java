package com.doscan.qrcode.standard.qrcode.input;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.charset.Charset;
import com.doscan.qrcode.standard.version.Version;
import com.doscan.qrcode.util.HexUtil;
import com.doscan.qrcode.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static com.doscan.qrcode.QREncoder.defaultCharset;

/**
 * 字节模式
 */
public class ByteInputThing extends InputThing {

    Charset charset;

    public ByteInputThing(Charset charset){
        this.charset = charset;
    }


    @Override
    public BitArray getModeIndicator() {
        BitArray indicator = new BitArray();
        indicator.appendBit(false);
        indicator.appendBit(true);
        indicator.appendBit(false);
        indicator.appendBit(false);
        return indicator;
    }

    @Override
    public int getCapNum(int codewordNum) {
        return codewordNum / 8 + 1;
    }

    @Override
    public boolean isMatch() {
        return false;
    }

    @Override
    public BitArray getBits() {

        if(!charset.equals(defaultCharset)){
            //
            byte[] bytes;
            try {
                bytes = this.content.getBytes(charset.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.bomb(" 不支持的字符集");
                return null;
            }
            BitArray bitArray = new BitArray();
            bitArray.appendBytes(bytes);
            return bitArray;
        }else{
            // 使用的默认字符集，则可以不做ECI处理
            return null;
        }

    }


    @Override
    public String getName() {
        return "字节模式";
    }

    @Override
    public BitArray getCountLength(Version version) {
        int numCount = content.length();
        String hexStr = HexUtil.intToBinaryStr(numCount,version.getCharIndicatorCount(this));
        BitArray bits = HexUtil.strToBitArray(hexStr);
        return bits;
    }
}

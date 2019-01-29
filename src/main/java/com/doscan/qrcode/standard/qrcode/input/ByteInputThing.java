package com.doscan.qrcode.standard.qrcode.input;

import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.standard.charset.Charset;
import com.doscan.qrcode.standard.version.Version;
import com.doscan.qrcode.util.HexUtil;
import com.doscan.qrcode.util.Log;

import java.io.UnsupportedEncodingException;

import static com.doscan.qrcode.QREncoder.defaultCharset;

/**
 * 字节模式
 */
public class ByteInputThing extends InputThing {

    Charset charset;
    private boolean isECI = false;
    byte[] bytes = new byte[0];

    public ByteInputThing(Charset charset){
        this.charset = charset;
        if(!charset.equals(defaultCharset)){
            isECI = true;
        }else{
            isECI = false;
        }
    }


    @Override
    public void content(String input) {
        super.content(input);
        try {
            bytes = this.content.getBytes(charset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.bomb(" 不支持的字符集");
        }
    }

    @Override
    public BitArray getModeIndicator() {

        BitArray indicator = new BitArray();

        if(isECI){
            // ECI 标识
            indicator.appendBit(false);
            indicator.appendBit(true);
            indicator.appendBit(true);
            indicator.appendBit(true);
            // ECI Designator
            CharacterSetECI eci = CharacterSetECI.getCharacterSetECIByName(charset.content());
            if (eci != null) {
                // 8bit 也是遵循zxing的风格，而非iso文档
                indicator.appendBits(eci.getValue(), 8);
            }
            indicator.appendBit(false);
            indicator.appendBit(true);
            indicator.appendBit(false);
            indicator.appendBit(false);
        }else{
            indicator.appendBit(false);
            indicator.appendBit(true);
            indicator.appendBit(false);
            indicator.appendBit(false);
        }


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

        BitArray bitArray = new BitArray();
        bitArray.appendBytes(bytes);
        return bitArray;

    }


    @Override
    public String getName() {
        return "字节模式";
    }

    @Override
    public BitArray getCountLength(Version version) {
        int numCount = bytes.length;
        String hexStr = HexUtil.intToBinaryStr(numCount,version.getCharIndicatorCount(this));
        BitArray bits = HexUtil.strToBitArray(hexStr);
        return bits;
    }
}

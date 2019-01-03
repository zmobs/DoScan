package com.doscan.qrcode.reedsolomon;

import java.util.ArrayList;
import java.util.List;

/**
 * 里德所罗门 算法编码器
 */
public class RSEncoder {

    private final List<GFPoly> cachedGenerators;
    private static RSEncoder instance;

    public static RSEncoder getInstance(){
        if(instance == null){
            instance = new RSEncoder();
        }
        return instance;
    }

    private RSEncoder(){
        cachedGenerators = new ArrayList<>();
        QRCodeGField qrCodeGField = new QRCodeGField();
//        cachedGenerators.add(GenericGFPoly(new int[]{1}));
    }

    public byte[] getRSCode(byte[] data,int totalNum){
        int errByteLen = totalNum - data.length;
        byte[] errBytes = new byte[errByteLen];
        return errBytes;
    }

}

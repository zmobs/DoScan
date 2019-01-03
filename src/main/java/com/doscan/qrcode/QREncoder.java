package com.doscan.qrcode;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.proto.QRCodeSymbol;
import com.doscan.qrcode.reedsolomon.RSEncoder;
import com.doscan.qrcode.standard.charset.Charset;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.doscan.qrcode.standard.qrcode.InputBitCaper;
import com.doscan.qrcode.standard.qrcode.input.InputThing;
import com.doscan.qrcode.standard.table.VersionECTable;
import com.doscan.qrcode.standard.version.Version;
import com.doscan.qrcode.standard.version.VersionDetector;
import com.doscan.qrcode.util.Log;

import java.util.Arrays;

/**
 * QRCode 编码器
 */
public class QREncoder {

    private IQRCode2015 type;
    private Version version;
    private Charset charset = Charset.ISO_8859_1;
    private String content;
    private ErrorCorrectLevel correctLevel;
    /**
     * 默认高质量策略
     */
    private EncodeStrategy encodeStrategy = EncodeStrategy.HIGN_QUALITY;


    public static QREncoder obain(){
        return new QREncoder();
    }

    public QREncoder strategy(EncodeStrategy strategy){
        this.encodeStrategy = strategy;
        return this;
    }

    public QREncoder correctLevel(ErrorCorrectLevel strategy){
        this.correctLevel = strategy;
        return this;
    }

    public QREncoder content(String input){
        this.content = input;
        return this;
    }

    public QREncoder version(int version){
        this.version = new Version(version);
        return this;
    }

    public QREncoder charset(Charset charset){

        if(charset == null){
            throw new BombException("字符集不能为空");
        }
        if(!java.nio.charset.Charset.isSupported(charset.content())){
            throw new BombException("系统字符集不支持");
        }

        this.charset = charset;
        return this;
    }


    /**
     * 进行组装编码的主流程
     * @return 编码完成的二维码定义
     */
    public QRCodeSymbol code(){

        InputResolver inputResolver = new InputResolver();
        InputThing inputThing = inputResolver.detect(content);

        // 如果没有手动指定version，则需要选择器智能选择
        VersionDetector.VersionCap versionCap;
        if(version == null || correctLevel == null){
            // 未指定版本信息以及纠错码级别，则自动需要自动选择
            versionCap = new VersionDetector(encodeStrategy)
                    .detectVersion(inputThing);
            if(versionCap != null){
                version = versionCap.getVersion();
                correctLevel = versionCap.getCorrectLevel();
            }
        }else{
            // 检查手动指定的参数，是否可以正确容纳
            boolean isCapAble = VersionDetector.checkSpecVersion(version,correctLevel,inputThing);
            if(!isCapAble){
                Log.bomb("指定版本不足以承载如此伟大的灵魂");
            }
            versionCap = new VersionDetector.VersionCap(version,correctLevel);
        }
        if(version == null){
            Log.bomb("版本错误");
        }
        // 获取到完整的数据区域bit序列
        BitArray finalBits = new InputBitCaper().getInputBits(versionCap,inputThing);
        /********************************  前方高能，，纠错码算法实现部分************************************/
        VersionECTable.ECBlockInfo ecBlockInfo = VersionECTable.instance
                .findBlockInfo(versionCap.getVersion().getVersionNumber(),versionCap.getCorrectLevel());
        int dateBlockByteNum = ecBlockInfo.getCapacityCodeword();
        Log.d("dateByteNum  ---   " + dateBlockByteNum);
        byte[] dataBytes = new byte[dateBlockByteNum];
        int byteOffset = 0;
        finalBits.toBytes(8 * byteOffset, dataBytes, dateBlockByteNum);
        Log.d("finalBits  ---   " + Arrays.toString(dataBytes));

        // 计算ec码字
        int ecBlockByteNum = ecBlockInfo.getECCodewordNum();
        Log.d("ecBlockByteNum  ---   " + ecBlockByteNum);

        //  进行编码
        byte[] ecBytes = RSEncoder.getInstance().getRSCode(dataBytes,ecBlockByteNum);
        /******************************************************************/
        // 根据指定的版本，进行填充拆分
        QRCodeSymbol qrCodeSymbol = new QRCodeSymbol();
        return qrCodeSymbol;

    }



}

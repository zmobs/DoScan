package com.doscan.qrcode;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.proto.EncodeStrategy;
import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.proto.QRCodeSymbol;
import com.doscan.qrcode.standard.charset.Charset;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.doscan.qrcode.standard.qrcode.input.InputThing;
import com.doscan.qrcode.standard.version.Version;
import com.doscan.qrcode.standard.version.VersionDetector;
import com.doscan.qrcode.util.Log;

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

        // 如果没有手动指定version，则需要选择器智能选择
        InputResolver inputResolver = new InputResolver();
        InputThing inputThing = inputResolver.detect(content);

        if(version == null || correctLevel == null){
            // todo
            // 未指定版本信息以及纠错码级别，则自动需要自动选择
            version = new VersionDetector(encodeStrategy)
                                    .detectVersion(inputThing);
        }else{
            // 检查手动指定的参数，是否可以正确容纳
            VersionDetector.checkSpecVersion(version,correctLevel,inputThing);
        }

        QRCodeSymbol qrCodeSymbol = new QRCodeSymbol();
        return qrCodeSymbol;

    }



}

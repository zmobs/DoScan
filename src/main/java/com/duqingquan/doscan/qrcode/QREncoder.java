package com.duqingquan.doscan.qrcode;

import com.duqingquan.doscan.qrcode.exception.BombException;
import com.duqingquan.doscan.qrcode.graphics.GraphicsHelper;
import com.duqingquan.doscan.qrcode.proto.BitArray;
import com.duqingquan.doscan.qrcode.proto.EncodeStrategy;
import com.duqingquan.doscan.qrcode.proto.IQRCode2015;
import com.duqingquan.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;
import com.duqingquan.doscan.qrcode.reedsolomon.QRBlockPair;
import com.duqingquan.doscan.qrcode.reedsolomon.RSEncoder;
import com.duqingquan.doscan.qrcode.standard.charset.Charset;
import com.duqingquan.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.duqingquan.doscan.qrcode.standard.qrcode.InputBitCaper;
import com.duqingquan.doscan.qrcode.standard.qrcode.input.InputThing;
import com.duqingquan.doscan.qrcode.standard.table.VersionECTable;
import com.duqingquan.doscan.qrcode.standard.version.Version;
import com.duqingquan.doscan.qrcode.standard.version.VersionDetector;
import com.duqingquan.doscan.qrcode.util.HexUtil;
import com.duqingquan.doscan.qrcode.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * QRCode 编码器
 */
public class QREncoder {

    private IQRCode2015 type;
    private Version version;
    private Charset charset = Charset.ISO_8859_1;
    public static final Charset defaultCharset  = Charset.ISO_8859_1;
    private String content;
    private ErrorCorrectLevel correctLevel;

    /**
     * 是否配置动画演示编码过程
     */
    private boolean animDemo = false;

    /**
     * 默认高质量策略
     */
    private EncodeStrategy encodeStrategy = EncodeStrategy.HIGN_QUALITY;


    public QREncoder setAnimDemo(boolean animDemo) {
        this.animDemo = animDemo;
        return this;
    }

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


        Log.defaultLog().record("键入文本 ： " + content);
        Log.defaultLog().record("指定编码 ： " + charset.content());

        InputResolver inputResolver = new InputResolver();
        InputThing inputThing = inputResolver.detect(content,charset,true);


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

        Log.defaultLog().record("计算得出QR 版本号为 ： " + version.getVersionNumber());
        Log.defaultLog().record("计算得出纠错级别为 ： " + correctLevel.getValue());
        // 获取到完整的数据区域bit序列
        // 7.4.1
        BitArray finalBits = new InputBitCaper().getInputBits(versionCap,inputThing);

        /********************************  前方高能，，纠错码算法实现部分************************************/
        VersionECTable.ECBlockInfo ecBlockInfo = VersionECTable.instance
                .findBlockInfo(versionCap.getVersion().getVersionNumber(),versionCap.getCorrectLevel());
        VersionECTable.ECBModel[] ecbModels = ecBlockInfo.getEcbs();
        int ecbNum = ecbModels.length;

        ArrayList<QRBlockPair> blocks = new ArrayList<>(ecbNum);

        // 记录以下最大的数据字节序列
        int maxNumDataBytes = 0;
        // 记录最大的纠错码字节序列
        int maxNumEcBytes = 0;
        // 记录数据序列中，每一个模块的Bit起始下标
        int byteOffset = 0;

        // 循环计算各个模块
        for(int i = 0; i < ecbNum;i++){

            // 获取当前正在计算的model块
            VersionECTable.ECBModel ecbModel = ecbModels[i];
            int dataBlockByteNum = ecbModel.getDataCodewordNum();

            // 承载数据的byte数组
            byte[] dataBytes = new byte[dataBlockByteNum];
            finalBits.toBytes(8 * byteOffset, dataBytes, dataBlockByteNum);

            /**
             * 生成多项式  /  消息多项式  =  纠错结果多项式
             */
            // 计算ec码字
            int ecBlockByteNum = ecBlockInfo.getECCodewordNum(i);
            //  得到纠错码编码
            int[] ecInts = RSEncoder.getInstance().getRSCode(dataBytes,ecBlockByteNum);

            byte[] ecBytes = HexUtil.intArrToByteArr(ecInts);

            Log.defaultLog().record("计算得到第" + i+"个数据数值" + Arrays.toString(ecInts));
            Log.defaultLog().record("计算得到第" + i+"个纠错码字数值" + Arrays.toString(ecBytes));

            QRBlockPair qrBlockPair = new QRBlockPair(dataBytes,ecBytes);
            blocks.add(qrBlockPair);

            maxNumDataBytes = Math.max(maxNumDataBytes, dataBlockByteNum);
            maxNumEcBytes = Math.max(maxNumEcBytes, ecBytes.length);
            byteOffset += dataBlockByteNum;

        }

        BitArray result = new BitArray();
        // 分别织入数据块和纠错块
        // 文档地址参看 7.6 的 说明15
        for(int i = 0; i < maxNumDataBytes;i++){
            // 拿当前一帧，不足则填充0
            for (QRBlockPair block : blocks) {
                byte[] dataBytes = block.getDataByteQuene();
                if (i < dataBytes.length) {
                    result.appendBits(dataBytes[i], 8);
                }else{
//                    // 填充0
//                    result.appendBits(0, 8);
                }
            }
        }

        for (int i = 0; i < maxNumEcBytes; ++i) {
            for (QRBlockPair block : blocks) {
                byte[] ecBytes = block.getEcByteQuene();
                // 纠错码一定是8的倍数，不会有填充case
                if (i < ecBytes.length) {
                    result.appendBits(ecBytes[i], 8);
                }
            }
        }

        Log.defaultLog().record("得到最终的对齐后的数据" + result);
        /*************************************************************/
        // 根据指定的版本，进行填充拆分
        QRCodeSymbol qrCodeSymbol = new QRCodeSymbol(version,correctLevel);
        qrCodeSymbol.placeData(result);
        if(this.animDemo){
            // 展示对话框
            GraphicsHelper graphicsHelper = new GraphicsHelper();
            graphicsHelper.showAnimDemo(qrCodeSymbol);
        }
        // 测试阶段，插播一个窗口
//        GraphicsHelper.showAnim(qrCodeSymbol);

        /************************************************************/
        return qrCodeSymbol;

    }



}

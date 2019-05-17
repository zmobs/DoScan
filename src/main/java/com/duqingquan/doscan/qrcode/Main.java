package com.duqingquan.doscan.qrcode;

import com.duqingquan.doscan.qrcode.output.IOutputer;
import com.duqingquan.doscan.qrcode.output.ImageOutputer;
import com.duqingquan.doscan.qrcode.proto.EncodeStrategy;
import com.duqingquan.doscan.qrcode.standard.charset.Charset;
import com.duqingquan.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;


public class Main {


    public static void main(String args[]) {

        QRCodeSymbol symbol = QREncoder.obain()
                // 手动指定容量版本和纠错级别
//                .version(2)
//                .correctLevel(ErrorCorrectLevel.M)
                // 自动选择符号符号信息
                .strategy(EncodeStrategy.HIGN_QUALITY)
                .charset(Charset.GB2312)
                .content("相信未来的我们，热血的灵魂。带着一颗赤子之心勇敢的狂奔")
                .setAnimDemo(false)
                .code();

        symbol
                .margin(6)
                .outputer(new ImageOutputer("D:\\111.PNG")).doOutout();

    }

}

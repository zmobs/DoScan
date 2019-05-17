package com.duqingquan.doscan.qrcode.output;

import com.duqingquan.doscan.qrcode.standard.qrcode.simple.QRCodeSymbol;

public interface IOutputer {

    boolean output(QRCodeSymbol symbol);

}

package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.standard.version.Version;
import com.doscan.qrcode.util.Log;

/**
 * QRCODE 符号实现定义
 */
public class QRCodeSymbol implements IQRCode2015 {

    // 最外侧是静默区域，并不是严格意义上的二维码组成部分

    // 三个定位符（包含分隔符）

    // 左下上，右左 两处版本信息区域

    // 左上，右下+左下右的两处格式信息

    // 两条分割线

    // todo 这里需要考虑多个数据块的因素
    // 修正符号(version > 1)

    public QRCodeSymbol(Version version){
        if(version == null){
            Log.bomb("构造版本不能为空");
        }
        DotTable dotTable = new DotTable(version.getSideModuleNum());
    }

}

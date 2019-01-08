package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.standard.table.DotTable;
import com.doscan.qrcode.standard.version.Version;
import com.doscan.qrcode.util.Log;

/**
 * QRCODE 符号实现定义
 */
public class QRCodeSymbol implements IQRCode2015 {
    /**
     * 点阵表
     */
    DotTable dotTable;
    // 最外侧是静默区域，并不是严格意义上的二维码组成部分

    // 三个定位符（包含分隔符）
    FinderPattern ltFP = new FinderPattern(FinderPattern.Position.LEFT_TOP);
    FinderPattern rtFP = new FinderPattern(FinderPattern.Position.RIGHT_TOP);
    FinderPattern lbFP = new FinderPattern(FinderPattern.Position.LEFT_BOTTOM);

    // 左下上，右左 两处版本信息区域

    // 左上，右下+左下右的两处格式信息

    // 两条分割线

    // todo 这里需要考虑多个数据块的因素
    // 修正符号(version > 1)

    public QRCodeSymbol(Version version){
        if(version == null){
            Log.bomb("构造版本不能为空");
        }
        dotTable = new DotTable(version.getSideModuleNum());
        // 放置定位符号
        ltFP.place(dotTable);
        rtFP.place(dotTable);
        lbFP.place(dotTable);
        //
    }

}

package com.doscan.qrcode.standard.qrcode.simple;

import com.doscan.qrcode.graphics.GraphicsHelper;
import com.doscan.qrcode.proto.BitArray;
import com.doscan.qrcode.proto.IQRCode2015;
import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.doscan.qrcode.standard.qrcode.mask.MaskEvaluator;
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
    public final int QUIET_ZONE_SIZE = 5;
    // 三个定位符（包含分隔符）
    FinderPattern ltFP = new FinderPattern(FinderPattern.Position.LEFT_TOP);
    FinderPattern rtFP = new FinderPattern(FinderPattern.Position.RIGHT_TOP);
    FinderPattern lbFP = new FinderPattern(FinderPattern.Position.LEFT_BOTTOM);

    // 孤独的小黑点 todo 需要定位文档说明地址
    LonelyBlackPoint lonelyBlackPoint = new LonelyBlackPoint();

    // 左下上，右左 两处版本信息区域

    // 左上，右下+左下右的两处格式信息
    FormatPattern formatPattern;
    // 两条分割线
    TimingPattern timingPattern;
    // todo 这里需要考虑多个数据块的因素
    // 修正符号(version > 1)
    AlignmentPattern alignmentPattern;

    VersionPattern versionPattern;
    /**
     * 数据编码区域
     */
    DataArea dataArea = new DataArea();

    public QRCodeSymbol(Version version, ErrorCorrectLevel correctLevel){

        if(version == null){
            Log.bomb("构造版本不能为空");
        }
        dotTable = new DotTable(version.getSideModuleNum());
        // 放置定位符号
        ltFP.place(dotTable);
        rtFP.place(dotTable);
        lbFP.place(dotTable);
        // 定位符号对应的分割线
        new SeparatorFP(ltFP).place(dotTable);
        new SeparatorFP(rtFP).place(dotTable);
        new SeparatorFP(lbFP).place(dotTable);
        // 孤独的小黑点
        lonelyBlackPoint.place(dotTable);
        // 矫正符号
        alignmentPattern = new AlignmentPattern(version.getVersionNumber());
        alignmentPattern.placeDots(dotTable);
        // 终止符号完成
        timingPattern = new TimingPattern(version);
        timingPattern.placeDots(dotTable);

        formatPattern = new FormatPattern(correctLevel);
        formatPattern.placeHold(dotTable);
        // 选择数据遮罩层
        versionPattern = new VersionPattern(version);
        versionPattern.placeDot(dotTable);

    }


    public void placeData(BitArray bitArray){
        // 置放数据区域的比特序列
        dataArea.place(dotTable,bitArray);
        MaskEvaluator maskEvaluator = new MaskEvaluator(formatPattern);
        int bestMask = maskEvaluator
                .evaluateMask(dotTable.getData(),dataArea.dataTable);
        bestMask = 3;
        maskEvaluator.embedMask(dotTable.getData(),dataArea.dataTable,bestMask);
    }

    public DotTable getDotTable() {
        return dotTable;
    }


}

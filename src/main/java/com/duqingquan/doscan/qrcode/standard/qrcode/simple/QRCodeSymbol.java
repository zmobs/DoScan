package com.duqingquan.doscan.qrcode.standard.qrcode.simple;

import com.duqingquan.doscan.qrcode.proto.BitArray;
import com.duqingquan.doscan.qrcode.proto.IQRCode2015;
import com.duqingquan.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.duqingquan.doscan.qrcode.standard.qrcode.mask.MaskEvaluator;
import com.duqingquan.doscan.qrcode.standard.table.DotTable;
import com.duqingquan.doscan.qrcode.standard.version.Version;
import com.duqingquan.doscan.qrcode.util.Log;
import com.duqingquan.doscan.qrcode.util.SystemUtil;

/**
 * QRCODE 符号实现定义
 */
public class QRCodeSymbol implements IQRCode2015 {
    /**
     * 点阵表,符号内部唯一得数据核心
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
    Version version;


    public QRCodeSymbol(Version version, ErrorCorrectLevel correctLevel){

        if(version == null){
            Log.bomb("构造版本不能为空");
        }
        this.version = version;
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
        animTable = new DotTable(version.getSideModuleNum());
    }



    DotTable animTable;
    public byte[][] obtainAnimData(){
        return animTable.getData();
    }

    /**
     * 带动画的绘制动作
     */
    public void bchWithAnim(){

    }
    /**
     * 带动画的绘制动作
     */
    public void plateWithAnim(){


        // 放置定位符号
        ltFP.place(animTable);
        SystemUtil.sleep(200);
        rtFP.place(animTable);
        SystemUtil.sleep(200);
        lbFP.place(animTable);
        SystemUtil.sleep(200);
        // 定位符号对应的分割线
        new SeparatorFP(ltFP).place(animTable);
        SystemUtil.sleep(200);
        new SeparatorFP(rtFP).place(animTable);
        SystemUtil.sleep(200);
        new SeparatorFP(lbFP).place(animTable);
        SystemUtil.sleep(200);
        // 孤独的小黑点
        lonelyBlackPoint.place(animTable);
        SystemUtil.sleep(200);

        // 矫正符号
        alignmentPattern.placeDots(animTable);
        SystemUtil.sleep(200);
        // 终止符号完成
        timingPattern.placeDots(animTable);
        SystemUtil.sleep(200);
        formatPattern.placeHold(animTable);
        SystemUtil.sleep(200);
        // 选择数据遮罩层
        versionPattern.placeDot(animTable);
        SystemUtil.sleep(200);
        // 内容绘制区域动画，重点啊
        if(hasPlaceData){
            dataArea.placeAnim(animTable);
            // 绘制遮罩层
            maskEvaluator.animAllMask(animTable,dataArea.dataTable);
            SystemUtil.sleep(200);
            byte[][] finalData = dotTable.getData().clone();
            animTable.setData(finalData);
        }

    }

    boolean hasPlaceData = false;
    MaskEvaluator maskEvaluator;
    int bestMask;
    public void placeData(BitArray bitArray){

        // 置放数据区域的比特序列
        dataArea.place(dotTable,bitArray);
        maskEvaluator = new MaskEvaluator(formatPattern);

        bestMask = maskEvaluator
                .evaluateMask(dotTable.getData(),dataArea.dataTable);
        maskEvaluator.embedMask(dotTable,dataArea.dataTable,bestMask);

        hasPlaceData = true;

    }

    public DotTable getDotTable() {
        return dotTable;
    }


}

package com.duqingquan.doscan.qrcode.standard.qrcode.simple;

import com.duqingquan.doscan.qrcode.proto.BitArray;
import com.duqingquan.doscan.qrcode.standard.table.DotTable;
import com.duqingquan.doscan.qrcode.standard.version.Version;
import com.duqingquan.doscan.qrcode.util.HexUtil;
import com.duqingquan.doscan.qrcode.util.Log;

/**
 * 版本信息区域（ version >= 7）
 */
public class VersionPattern {

    Version ver;

    VersionPattern(Version version){
        this.ver = version;
    }

    public void placeHold(DotTable dotTable){

        int sideNum = dotTable.getSideSize();
        int versionSideNum = new Version(7).getSideModuleNum();
        byte[][] data = dotTable.getData();

        if(sideNum >= versionSideNum){
            // 占位
            int startX = sideNum - 7 - 3 - 1;
            for(int i = 0 ; i < 6 ; i++){
                data[startX][i] = -2;
                data[startX + 1][i] = -2;
                data[startX + 2][i] = -2;

                data[i][startX] = -2;
                data[i][startX + 1] = -2;
                data[i][startX + 2] = -2;
            }
        }
    }

    /**
     * 真实的填充数据
     * @param dotTable
     */
    public void placeDot(DotTable dotTable){

        // 生成version信息
        BitArray bitArray = generateVersionBit();

        int sideNum = dotTable.getSideSize();
        int versionSideNum = new Version(7).getSideModuleNum();
        byte[][] data = dotTable.getData();

        if(sideNum >= versionSideNum){
            // 占位
            int startX = sideNum - 7 - 3 - 1;
            for(int i = 0 ; i < 6 ; i++){

                byte first = bitArray.get(3 * i) ? (byte)1 : (byte)0;
                byte second = bitArray.get(3 * i + 1) ? (byte)1 : (byte)0;
                byte third = bitArray.get(3 * i + 2) ? (byte)1 : (byte)0;

                data[startX][i] = first;
                data[startX + 1][i] = second;
                data[startX + 2][i] = third;

                data[i][startX] = first;
                data[i][startX + 1] = second;
                data[i][startX + 2] = third;

            }
        }
    }

    /**
     * 固定的生成多项式系数， 文档地址是ISO 180004 2015  ANNEX d.1
     */
    private static final int VERSION_INFO_POLY = 0x1f25;  // 1 1111 0010 0101
    final int POLAY_OFFSET_NUM = 12;
    /**
     * 多项式中 zero的位置
     */
    final int msbSetInPoly = 13;

    private BitArray generateVersionBit(){
        int versionNum = ver.getVerNum();
        String bStr = HexUtil.intToBinaryStr(versionNum,6);
        BitArray bits = HexUtil.strToBitArray(bStr);
        // 进行计算BCH
        int srcPoly = versionNum << POLAY_OFFSET_NUM;
        // 不断 异或操作 生成多项式，得到最后的纠错码字
        while (findMSBSet(srcPoly) >= msbSetInPoly) {
            // 对齐后异或操作，直到得到的结果位数小于13
            srcPoly ^= VERSION_INFO_POLY << (findMSBSet(srcPoly) - msbSetInPoly);
        }
        // 和zxing 有点区别，没有考虑遮罩码，这是因为我们标准文档2015比较新
        bits.appendBits(srcPoly,12);
        Log.d("bits  ---  " + bits);
        return bits;
    }

    static int findMSBSet(int value) {
        return 32 - Integer.numberOfLeadingZeros(value);
    }

}

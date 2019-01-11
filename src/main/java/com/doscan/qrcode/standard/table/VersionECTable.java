package com.doscan.qrcode.standard.table;

import com.doscan.qrcode.standard.qrcode.ErrorCorrectLevel;
import com.doscan.qrcode.util.Log;

import java.util.ArrayList;

/**
 * ISO-18004-2018-12-25  TABLE 7
 */
public class VersionECTable{

    /**
     * 实例对象
     */
    public static VersionECTable instance = new VersionECTable();
    /**
     * 版本数据容器
     */
    private ArrayList<VersionCapacityItem> data = new ArrayList<>(40);

    private VersionECTable(){
        buildTable();
    }


    private void buildTable() {
        // 1
        data.add(new VersionCapacityItem(
                1,
                new ECBlockInfo(2,new ECBModel(26,19)),
                new ECBlockInfo(4,new ECBModel(26,16)),
                new ECBlockInfo(6,new ECBModel(26,13)),
                new ECBlockInfo(8,new ECBModel(26,9))));
        // 2
        data.add(new VersionCapacityItem(
                2,
                new ECBlockInfo(4,new ECBModel(44,34)),
                new ECBlockInfo(8,new ECBModel(44,28)),
                new ECBlockInfo(11,new ECBModel(44,22)),
                new ECBlockInfo(14,new ECBModel(44,16))));
        // 3
        data.add(new VersionCapacityItem(
                3,
                new ECBlockInfo(7,
                        new ECBModel(70,55)),
                new ECBlockInfo(13,
                        new ECBModel(70,44)),
                new ECBlockInfo(9,
                                    new ECBModel(35,17),
                                    new ECBModel(35,17)),
                new ECBlockInfo(11,
                                    new ECBModel(35,13),
                                    new ECBModel(35,13))));

        // 4
        data.add(new VersionCapacityItem(
                4,
                new ECBlockInfo(10,
                        new ECBModel(100,80)),
                new ECBlockInfo(9,
                        new ECBModel(50,32),
                        new ECBModel(50,32)),
                new ECBlockInfo(13,
                        new ECBModel(50,24),
                        new ECBModel(50,24)),
                new ECBlockInfo(8,
                        new ECBModel(25,9),
                        new ECBModel(25,9),
                        new ECBModel(25,9),
                        new ECBModel(25,9))));

        // 5
        data.add(new VersionCapacityItem(
                5,
                new ECBlockInfo(13,
                        new ECBModel(134,108)),
                new ECBlockInfo(12,
                        new ECBModel(67,43),
                        new ECBModel(67,43)),
                new ECBlockInfo(9,
                        new ECBModel(33,15),
                        new ECBModel(33,15),
                        new ECBModel(34,16),
                        new ECBModel(34,16)),
                new ECBlockInfo(11,
                        new ECBModel(33,11),
                        new ECBModel(33,11),
                        new ECBModel(34,12),
                        new ECBModel(34,12))));
        // 6
        data.add(new VersionCapacityItem(
                6,
                new ECBlockInfo(9,
                        new ECBModel(86,68),
                        new ECBModel(86,68)),
                new ECBlockInfo(8,
                        new ECBModel(43,27),
                        new ECBModel(43,27),
                        new ECBModel(43,27),
                        new ECBModel(43,27)),
                new ECBlockInfo(12,
                        new ECBModel(43,19),
                        new ECBModel(43,19),
                        new ECBModel(43,19),
                        new ECBModel(43,19)),
                new ECBlockInfo(14,
                        new ECBModel(43,15),
                        new ECBModel(43,15),
                        new ECBModel(43,15),
                        new ECBModel(43,15))));
        // 7
        data.add(new VersionCapacityItem(
                7,
                new ECBlockInfo(10,
                        new ECBModel(98,78),
                        new ECBModel(98,78)),
                new ECBlockInfo(9,
                        new ECBModel(49,31),
                        new ECBModel(49,31),
                        new ECBModel(49,31),
                        new ECBModel(49,31)),
                new ECBlockInfo(9,
                        new ECBModel(32,14),
                        new ECBModel(32,14),
                        new ECBModel(33,15),
                        new ECBModel(33,15),
                        new ECBModel(33,15),
                        new ECBModel(33,15)),
                new ECBlockInfo(13,
                        new ECBModel(39,13),
                        new ECBModel(39,13),
                        new ECBModel(39,13),
                        new ECBModel(39,13),
                        new ECBModel(40,14))));
        // 8
        data.add(new VersionCapacityItem(
                8,
                new ECBlockInfo(12,
                        new ECBModel(121,97),
                        new ECBModel(121,97)),
                new ECBlockInfo(11,
                        new ECBModel(60,38),
                        new ECBModel(60,38),
                        new ECBModel(61,39),
                        new ECBModel(61,39)),
                new ECBlockInfo(11,
                        new ECBModel(40,18),
                        new ECBModel(40,18),
                        new ECBModel(40,18),
                        new ECBModel(40,18),
                        new ECBModel(41,19),
                        new ECBModel(41,19)),
                new ECBlockInfo(13,
                        new ECBModel(40,14),
                        new ECBModel(40,14),
                        new ECBModel(40,14),
                        new ECBModel(40,14),
                        new ECBModel(41,15),
                        new ECBModel(41,15))));
        //  todo 需要添加8以后的版本数据

    }


    public ECBlockInfo findBlockInfo(int version, ErrorCorrectLevel errorCorrectLevel){
        int versionIndex = version - 1;
        if(versionIndex > data.size()){
            Log.bomb("检索版本过大，不支持");
        }
        VersionCapacityItem versionCapacityItem = data.get(versionIndex);
        switch (errorCorrectLevel){
            case H:
                return versionCapacityItem.H;
            case L:
                return versionCapacityItem.L;
            case Q:
                return versionCapacityItem.Q;
            case M:
                return versionCapacityItem.M;
            default:
                Log.bomb("error ec level");
        }
        return null;
    }

    /**
     * ecb块的信息描述
     */
    public final static class ECBlockInfo{
        /**
         * 数据块的数量
         */
        ECBModel[] ecb;
        /**
         * 纠错码字数量
         */
        int ecCodewordNum;

        ECBlockInfo(int ecCodewordNum,ECBModel... ecb){
            this.ecCodewordNum = ecCodewordNum;
            this.ecb = ecb;
        }

        public int getEcbNum(){
            return ecb.length;
        }

        public ECBModel[] getEcbs(){
            return ecb;
        }

        /**
         * 获取指定版本，指定纠错的容量 码字数
         * @return
         */
        public int getCapacityCodeword(){
            int codeLength = 0;
            for(ECBModel ecbModel : ecb){
                codeLength += ecbModel.dataCodewordNum;
            }
            return codeLength;
        }


        public int getECCodewordNum(){
            int codeLength = 0;
            for(ECBModel ecbModel : ecb){
                int ecNum = ecbModel.totalCodewordNum - ecbModel.dataCodewordNum;
                codeLength += ecNum;
            }
            return codeLength;
        }

    }

    /**
     * ECB 数据模型
     */
    final public class ECBModel{

        int dataCodewordNum;
        int totalCodewordNum;

        ECBModel(int totalCodewordNum,int dataCodewordNum){
            this.dataCodewordNum = dataCodewordNum;
            this.totalCodewordNum = totalCodewordNum;
        }


        public int getDataCodewordNum() {
            return dataCodewordNum;
        }

        public int getTotalCodewordNum() {
            return totalCodewordNum;
        }

    }

    final class VersionCapacityItem {

        int versionNum;
        ECBlockInfo L;
        ECBlockInfo M;
        ECBlockInfo Q;
        ECBlockInfo H;

        public VersionCapacityItem(int versionNum,
                                   ECBlockInfo l,
                                   ECBlockInfo m,
                                   ECBlockInfo q,
                                   ECBlockInfo h) {
            this.versionNum = versionNum;
            L = l;
            M = m;
            Q = q;
            H = h;
        }


    }


}

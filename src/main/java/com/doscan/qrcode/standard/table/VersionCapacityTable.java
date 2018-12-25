package com.doscan.qrcode.standard.table;

import java.util.HashMap;

/**
 * ISO-18004-2018-12-25  TABLE 7
 */
public class VersionCapacityTable implements ITable{

//    private static VersionCapacityTable instance = buildTable();
    private HashMap<Integer,CapacityItem> data = new HashMap<>(40);

    @Override
    public void buildTable() {

    }

//    private static VersionCapacityTable buildTable(){
//        VersionCapacityTable versionCapacityTable = new VersionCapacityTable();
//        return versionCapacityTable;
//    }


    class CapacityItem{
        int versionNum;
        int allCapacityCodeword;
        int allCapacityBit;
        int numericCapacity;
        int alphaNumCapacity;
        int byteCapacity;
        int kanjiCapacity;
    }


}

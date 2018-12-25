package com.doscan.qrcode.standard.table;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ISO-18004-2018-12-25  TABLE 7
 */
public class VersionECTable{

//    private static VersionECTable instance = buildTable();
    private ArrayList<CapacityItem> data = new ArrayList<>(40);

    public void buildTable() {
        data.add(new CapacityItem(1,7,10,13,17));
        data.add(new CapacityItem(2,10,16,22,28));
        data.add(new CapacityItem(3,15,26,36,40));
        data.add(new CapacityItem(4,7,10,13,17));
        data.add(new CapacityItem(5,7,10,13,17));
    }



    class CapacityItem{

        int versionNum;
        int L;
        int M;
        int Q;
        int H;

        public CapacityItem(int versionNum, int l, int m, int q, int h) {
            this.versionNum = versionNum;
            L = l;
            M = m;
            Q = q;
            H = h;
        }

        public int getVersionNum() {
            return versionNum;
        }

        public void setVersionNum(int versionNum) {
            this.versionNum = versionNum;
        }

        public int getL() {
            return L;
        }

        public void setL(int l) {
            L = l;
        }

        public int getM() {
            return M;
        }

        public void setM(int m) {
            M = m;
        }

        public int getQ() {
            return Q;
        }

        public void setQ(int q) {
            Q = q;
        }

        public int getH() {
            return H;
        }

        public void setH(int h) {
            H = h;
        }
    }


}

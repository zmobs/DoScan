package com.doscan.qrcode.reedsolomon;

/**
 * 因为纠错码需要拆分的"块"定义
 */
final public class QRBlockPair {
    /**
     * 数据字节序列
     */
    private byte[] dataByteQuene;
    /**
     * 纠错码字节序列
     */
    private byte[] ecByteQuene;

    public QRBlockPair(byte[] dataByteQuene, byte[] ecByteQuene) {
        this.dataByteQuene = dataByteQuene;
        this.ecByteQuene = ecByteQuene;
    }

    public byte[] getDataByteQuene() {
        return dataByteQuene;
    }

    public byte[] getEcByteQuene() {
        return ecByteQuene;
    }

}

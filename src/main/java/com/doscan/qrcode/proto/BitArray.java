package com.doscan.qrcode.proto;

import com.doscan.qrcode.exception.HumingException;

/**
 * 节省空间的bit容器
 */
public class BitArray {

    private int size;
    private int[] bits;

    BitArray(){
        //32bit
        bits = new int[1];
    }


    public int getSize() {
        return size;
    }

    public int getSizeInBytes() {
        return (size + 7) / 8;
    }

    /**
     * 保证容量可用
     * @param size
     */
    private void ensureCapacity(int size) {

        if (size > bits.length * 32) {
            int[] newBits = makeArray(size);
            System.arraycopy(bits, 0, newBits, 0, bits.length);
            this.bits = newBits;
        }
    }

    /**
     * 构建数组
     * @param size
     * @return
     */
    private static int[] makeArray(int size) {
        return new int[(size + 31) / 32];
    }

}

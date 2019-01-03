package com.doscan.qrcode.proto;

import com.doscan.qrcode.exception.BombException;
import com.doscan.qrcode.exception.HumingException;

/**
 * 节省空间的bit容器
 */
public class BitArray {

    /**
     * 实际储存的比特序列长度，因为会有荒野比特存在
     */
    private int size = 0;
    /**
     * 用来存在比特数据的int单元，会有荒野区域存在
     */
    private int[] bits;

    public BitArray(){
        // 32bit default
        bits = new int[1];
    }


    public BitArray(int defaultSize){
        // 32bit default
        bits = new int[1];
        this.size = defaultSize;
    }

    /**
     * 转换为8比特
     * @param bitOffset
     * @param array
     * @param numBytes
     */
    public void toBytes(int bitOffset, byte[] array, int numBytes) {
        for (int i = 0; i < numBytes; i++) {
            // 默认 00000000
            int theByte = 0;
            // 连续读取八个比特，封装进入一个字节
            for (int j = 0; j < 8; j++) {
                // todo 需要重写这部分
                if (get(bitOffset)) {
                    int mask = 1 << (7 -j);
                    theByte |= mask;
                }
                bitOffset++;
            }
            array[i] = (byte) theByte;
        }
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

    /**
     * 获取制定位置的比特
     * @param i
     * @return
     */
    public boolean get(int i) {
        // 定位到指定的容器int
        int bitUnit = bits[i / 32];
        // 计算bit 在int中对应的下标1F = 31 取前32位
        int bitUnitIndex = i & 0x1F;
        // 对1进行位移操作，得到特定位置的一位遮罩层
        int bitMask = 1 << bitUnitIndex;

        // 储存单元和int和遮罩层按位与获取结果
        return (bitUnit & bitMask) != 0;
    }

    /**
     * 设置定位的数据单元为1
     * @param i  比特下标
     */
    public void set(int i) {

        int unitIndex = i / 32;
        // 计算bit 在int中对应的下标1F = 31 取前32位
        int bitUnitIndex = i & 0x1F;
        // 对1进行位移操作，得到特定位置的一位遮罩层
        int bitMask = 1 << bitUnitIndex;

        bits[unitIndex] |= bitMask;

    }


    /**
     * 对指定比特位置进行反转
     * @param i 需要转换的比特下标
     */
    public void flip(int i) {
        int unitIndex = i / 32;
        // 计算bit 在int中对应的下标1F = 31 取前32位
        int bitUnitIndex = i & 0x1F;
        // 对1进行位移操作，得到特定位置的一位遮罩层
        int bitMask = 1 << bitUnitIndex;

        bits[unitIndex] ^= bitMask;
    }

    /**
     * 清空所有的比特集合
     */
    public void clear() {
        int max = bits.length;
        for (int i = 0; i < max; i++) {
            bits[i] = 0;
        }
    }

    /**
     * 向尾部添加比特数据
     * @param bit 添加数据
     */
    public void appendBit(boolean bit) {

        ensureCapacity(size + 1);

        if (bit) {
            //  1 需要手动处理，0 不需要
            int bitMask = 1 << (size & 0x1F);
            bits[size / 32] |= bitMask;
        }

        size += 1;
    }


    /**
     * 向尾部添加比特序列
     * @param value 比特容器int
     * @param numBits 想要添加的比特数量
     */
    public void appendBits(int value, int numBits) {

        if (numBits < 0 || numBits > 32) {
            throw new BombException("Num bits must be between 0 and 32");
        }
        // 保证不越界
        ensureCapacity(size + numBits);

        for (int numBitsLeft = numBits; numBitsLeft > 0; numBitsLeft--) {
            // 循环添加
            int willAddNum = value >> (numBitsLeft - 1);
            int willAddBit = willAddNum & 0x01;
            appendBit((willAddBit) == 1);
        }
    }


    /**
     * 添加比特队列对象的函数实现
     * @param other
     */
    public void appendBitArray(BitArray other) {
        int otherSize = other.getSize();
        ensureCapacity(size + otherSize);
        for (int i = 0; i < otherSize; i++) {
            appendBit(other.get(i));
        }
    }


    /**
     * 进行xor操作
     * @param other
     */
    public void xor(BitArray other) {
        if (size != other.size) {
            throw new BombException("Sizes don't match");
        }
        for (int i = 0; i < bits.length; i++) {
            // The last int could be incomplete (i.e. not have 32 bits in
            // it) but there is no problem since 0 XOR 0 == 0.
            bits[i] ^= other.bits[i];
        }
    }


    @Override
    public String toString() {

        StringBuilder result = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            result.append(get(i) ? 'x' : 'o');
        }
        return result.toString();
    }

}

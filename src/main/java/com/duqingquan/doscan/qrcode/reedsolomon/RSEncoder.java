package com.duqingquan.doscan.qrcode.reedsolomon;

import java.util.ArrayList;
import java.util.List;

/**
 * 里德所罗门 算法编码器
 */
public class RSEncoder {

    /**
     * 生成多项式公式缓存
     */
    private final List<GFPoly> cachedGenerators;
    private static RSEncoder instance;
    QRCodeGField qrCodeGField = new QRCodeGField();

    public static RSEncoder getInstance(){
        if(instance == null){
            instance = new RSEncoder();
        }
        return instance;
    }

    private RSEncoder(){
        cachedGenerators = new ArrayList<>();
        cachedGenerators.add(new GFPoly(qrCodeGField,new int[]{1}));
    }

    /**
     * 创建多项式，并且缓存之前的生成多项式公式
     * @param degree  多项式公式数
     * @return
     */
    private GFPoly buildAndCachePolys(int degree){

        if (degree >= cachedGenerators.size()) {
            GFPoly lastGenerator = cachedGenerators.get(cachedGenerators.size() - 1);
            for (int d = cachedGenerators.size(); d <= degree; d++) {
                GFPoly nextGenerator = lastGenerator.multiply(
                        new GFPoly(qrCodeGField,
                                new int[] { 1, qrCodeGField.exp(d - 1 + qrCodeGField.GENERATOR_BASE) }));

                cachedGenerators.add(nextGenerator);
                lastGenerator = nextGenerator;
            }
        }
        return cachedGenerators.get(degree);
    }

    /**
     * 根据数据字序列和纠错码长度，生成rs纠错码序列
     * @param data
     * @param errLength
     * @return
     */
    public int[] getRSCode(byte[] data,int errLength){
        int[] errBytes = new int[errLength];

        // 伽罗瓦域里面没有负数，所以需要遍历取正
        int[] dataInts = new int[data.length];
        for (int i = 0; i < dataInts.length; i++) {
            dataInts[i] = data[i] & 0xFF;
        }

        //  先获取并且缓存多项式公式
        // 得到了生成多项式
        GFPoly targetGFPloy = buildAndCachePolys(errLength);

        // 计算消息多项式，首先是获取系数，他们就是消息本身
        int[] infoCoefficients = new int[data.length];
        System.arraycopy(dataInts, 0, infoCoefficients, 0, data.length);

        // 对信息进行多项式运算
        GFPoly info = new GFPoly(qrCodeGField, infoCoefficients);
        // 对信息多项式乘以单项式  纠错码长度的1次方
        info = info.multiplyByMonomial(errLength, 1);
        // 生成多项式，取余消息多项式，得到纠错码多项式
        GFPoly remainder = info.divide(targetGFPloy)[1];
        // 获取纠错多项式因数
        int[] coefficients = remainder.getCoefficients();
        // 如果因子不足位，则需要前置位填充0
        int numZeroCoefficients = errLength - coefficients.length;
        for (int i = 0; i < numZeroCoefficients; i++) {
            errBytes[i] = 0;
        }
        System.arraycopy(coefficients,0,errBytes,numZeroCoefficients,coefficients.length);
        return errBytes;
    }




}

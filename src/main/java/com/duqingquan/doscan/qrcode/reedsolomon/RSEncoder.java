package com.duqingquan.doscan.qrcode.reedsolomon;

import com.duqingquan.doscan.qrcode.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int[] decodeRSCode(byte[] data,int dataLength){

        int[] codewordsInts = new int[dataLength];
        for (int i = 0; i < dataLength; i++) {
            codewordsInts[i] = data[i] & 0xFF;
        }
        Log.d("codewordsInts  ---  "  + Arrays.toString(codewordsInts));

        int errLength = data.length - dataLength;
        int[] syndromeCoefficients = new int[errLength];

        GFPoly poly = new GFPoly(qrCodeGField, codewordsInts);
        for (int i = 0; i < errLength; i++) {
            int eval = poly.evaluateAt(qrCodeGField.exp(i + qrCodeGField.GENERATOR_BASE));
            syndromeCoefficients[syndromeCoefficients.length - 1 - i] = eval;
            Log.d("eval  ---  "  + i + "  " + eval);
        }

        GFPoly syndrome = new GFPoly(qrCodeGField, syndromeCoefficients);
        GFPoly[] sigmaOmega =
                runEuclideanAlgorithm(qrCodeGField.buildMonomial(errLength, 1), syndrome, errLength);
        GFPoly sigma = sigmaOmega[0];
        GFPoly omega = sigmaOmega[1];
        int[] errorLocations = findErrorLocations(sigma);
        int[] errorMagnitudes = findErrorMagnitudes(omega, errorLocations);
        for (int i = 0; i < errorLocations.length; i++) {
            int position = codewordsInts.length - 1 - qrCodeGField.log(errorLocations[i]);
            if (position < 0) {
                Log.bomb("Bad error location");
            }
            codewordsInts[position] = qrCodeGField.addOrSubtract(codewordsInts[position], errorMagnitudes[i]);
        }

        return codewordsInts;
    }

    private int[] findErrorMagnitudes(GFPoly errorEvaluator, int[] errorLocations) {
        // This is directly applying Forney's Formula
        int s = errorLocations.length;
        int[] result = new int[s];
        for (int i = 0; i < s; i++) {
            int xiInverse = qrCodeGField.inverse(errorLocations[i]);
            int denominator = 1;
            for (int j = 0; j < s; j++) {
                if (i != j) {
                    //denominator = field.multiply(denominator,
                    //    QRCodeGField.addOrSubtract(1, field.multiply(errorLocations[j], xiInverse)));
                    // Above should work but fails on some Apple and Linux JDKs due to a Hotspot bug.
                    // Below is a funny-looking workaround from Steven Parkes
                    int term = qrCodeGField.multiply(errorLocations[j], xiInverse);
                    int termPlus1 = (term & 0x1) == 0 ? term | 1 : term & ~1;
                    denominator = qrCodeGField.multiply(denominator, termPlus1);
                }
            }
            result[i] = qrCodeGField.multiply(errorEvaluator.evaluateAt(xiInverse),
                    qrCodeGField.inverse(denominator));
            if (qrCodeGField.GENERATOR_BASE != 0) {
                result[i] = qrCodeGField.multiply(result[i], xiInverse);
            }
        }
        return result;
    }


    private GFPoly[] runEuclideanAlgorithm(GFPoly a, GFPoly b, int R)
            {
        // Assume a's degree is >= b's
        if (a.getDegree() < b.getDegree()) {
            GFPoly temp = a;
            a = b;
            b = temp;
        }

        GFPoly rLast = a;
        GFPoly r = b;
        GFPoly tLast = qrCodeGField.getZero();
        GFPoly t = qrCodeGField.getOne();

        // Run Euclidean algorithm until r's degree is less than R/2
        while (r.getDegree() >= R / 2) {
            GFPoly rLastLast = rLast;
            GFPoly tLastLast = tLast;
            rLast = r;
            tLast = t;

            // Divide rLastLast by rLast, with quotient in q and remainder in r
            if (rLast.isZero()) {
                // Oops, Euclidean algorithm already terminated?
                Log.bomb("r_{i-1} was zero");
            }
            r = rLastLast;
            GFPoly q = qrCodeGField.getZero();
            int denominatorLeadingTerm = rLast.getCoefficient(rLast.getDegree());
            int dltInverse = qrCodeGField.inverse(denominatorLeadingTerm);
            while (r.getDegree() >= rLast.getDegree() && !r.isZero()) {
                int degreeDiff = r.getDegree() - rLast.getDegree();
                int scale = qrCodeGField.multiply(r.getCoefficient(r.getDegree()), dltInverse);
                q = q.addOrSubtract(qrCodeGField.buildMonomial(degreeDiff, scale));
                r = r.addOrSubtract(rLast.multiplyByMonomial(degreeDiff, scale));
            }

            t = q.multiply(tLast).addOrSubtract(tLastLast);

            if (r.getDegree() >= rLast.getDegree()) {
                throw new IllegalStateException("Division algorithm failed to reduce polynomial?");
            }
        }

        int sigmaTildeAtZero = t.getCoefficient(0);
        if (sigmaTildeAtZero == 0) {
            Log.bomb("sigmaTilde(0) was zero");
        }

        int inverse = qrCodeGField.inverse(sigmaTildeAtZero);
        GFPoly sigma = t.multiply(inverse);
        GFPoly omega = r.multiply(inverse);
        return new GFPoly[]{sigma, omega};
    }

    private int[] findErrorLocations(GFPoly errorLocator) {
        // This is a direct application of Chien's search
        int numErrors = errorLocator.getDegree();
        if (numErrors == 1) { // shortcut
            return new int[] { errorLocator.getCoefficient(1) };
        }
        int[] result = new int[numErrors];
        int e = 0;
        for (int i = 1; i < qrCodeGField.getSize() && e < numErrors; i++) {
            if (errorLocator.evaluateAt(i) == 0) {
                result[e] = qrCodeGField.inverse(i);
                e++;
            }
        }
        if (e != numErrors) {
            Log.bomb("Error locator degree does not match number of roots");
        }
        return result;
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

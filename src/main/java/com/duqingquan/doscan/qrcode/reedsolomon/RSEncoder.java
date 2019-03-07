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

    /**
     * 前方高能，下面是具体的里德所罗门算法解码部分实现
     * @param data
     * @param dataLength
     * @return
     */
    public int[] decodeRSCode(byte[] data,int dataLength){

        // 先将字节数据展开为int,使用的位运算，效率高
        int[] codewordsInts = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            codewordsInts[i] = data[i] & 0xFF;
        }

        int errLength = data.length - dataLength;
        // 对纠错码占据的位置，进行系数运算和比较
        int[] syndromeCoefficients = new int[errLength];
        GFPoly poly = new GFPoly(qrCodeGField, codewordsInts);
        for (int i = 0; i < errLength; i++) {
            // 计算每个位置的纠错码
            int eval = poly.evaluateAt(qrCodeGField.exp(i));
            // 倒序逐个防止eval至数组
            syndromeCoefficients[errLength - 1 - i] = eval;
            Log.d("eval  ---  "  + i + "  " + eval);
        }

        // 需要进行纠错，将出现状况的特征码再次放入有限域
        GFPoly syndrome = new GFPoly(qrCodeGField, syndromeCoefficients);
        // sigma omega
        GFPoly[] sigmaOmega =
                runEuclideanAlgorithm(qrCodeGField.buildMonomial(errLength, 1), syndrome, errLength);
        // 得到 Σ和Ω
        GFPoly sigma = sigmaOmega[0];
        GFPoly omega = sigmaOmega[1];
        // 找到一组 错误位置的下标
        int[] errorLocations = findErrorLocations(sigma);
        // 计算得到错误位置对应的一组 错误偏移量
        int[] errorMagnitudes = findErrorMagnitudes(omega, errorLocations);
        // 对错误位置，进行逆向偏移，得到纠错后的结果
        for (int i = 0; i < errorLocations.length; i++) {
            int position = codewordsInts.length - 1 - qrCodeGField.log(errorLocations[i]);
            if (position < 0) {
                Log.bomb("Bad error location");
            }
            codewordsInts[position] = qrCodeGField.addOrSubtract(codewordsInts[position], errorMagnitudes[i]);
        }
        // 返回结果
        return codewordsInts;
    }

    /**
     * 寻找错误的偏移量
     * @param errorEvaluator
     * @param errorLocations
     * @return
     */
    private int[] findErrorMagnitudes(GFPoly errorEvaluator, int[] errorLocations) {
        // This is directly applying Forney's Formula
        // 直接使用福尼公式
        int s = errorLocations.length;
        int[] result = new int[s];
        // 遍历所有的出错位置
        for (int i = 0; i < s; i++) {
            int xiInverse = qrCodeGField.inverse(errorLocations[i]);
            int denominator = 1;
            for (int j = 0; j < s; j++) {
                if (i != j) {
                    //denominator = field.multiply(denominator,
                    //    QRCodeGField.addOrSubtract(1, field.multiply(errorLocations[j], xiInverse)));
                    // 上面的代码是应该生效的，但是因为苹果和linux系统上 Hotspot虚拟机存在一些Bug，所有无法使用。
                    // 下面是一段看上去不是很优雅的的替代方案
                    // Above should work but fails on some Apple and Linux JDKs due to a Hotspot bug.
                    // Below is a funny-looking workaround from Steven Parkes
                    int term = qrCodeGField.multiply(errorLocations[j], xiInverse);
                    int termPlus1 = (term & 0x1) == 0 ? term | 1 : term & ~1;
                    // 计算得到分母的数值
                    denominator = qrCodeGField.multiply(denominator, termPlus1);
                }
            }
            // 计算得到错误位置的偏移量
            result[i] = qrCodeGField.multiply(errorEvaluator.evaluateAt(xiInverse),
                    qrCodeGField.inverse(denominator));
            if (qrCodeGField.GENERATOR_BASE != 0) {
                result[i] = qrCodeGField.multiply(result[i], xiInverse);
            }
        }
        return result;
    }

    /**
     * 执行 欧几里得算法，得到两个有限域的最大公约数
     * @param a 有限域A
     * @param b 有限域B
     * @param R 期望结果数值的长度
     * @return
     */
    private GFPoly[] runEuclideanAlgorithm(GFPoly a, GFPoly b, int R) {
        //https://www.jianshu.com/p/7876eb2dff89
        // 如果A的度数，大于b的度数， 进行换位操作
        if (a.getDegree() < b.getDegree()) {
            GFPoly temp = a;
            a = b;
            b = temp;
        }

        GFPoly rLast = a;
        GFPoly r = b;
        // 这是一个纯0的 有限域多项式
        GFPoly tLast = qrCodeGField.getZero();
        // 这是一个纯1的有限域多项式
        GFPoly t = qrCodeGField.getOne();

        // Run Euclidean algorithm until r's degree is less than R/2
        // 迭代运行欧几里得算法，直到余数的长度小于二分之一的纠错码长度
        while (r.getDegree() >= R / 2) {
            GFPoly rLastLast = rLast;
            GFPoly tLastLast = tLast;
            rLast = r;
            tLast = t;

            // Divide rLastLast by rLast, with quotient in q and remainder in r
            // 余数辗转相除，也就是欧几里得算法实现了
            if (rLast.isZero()) {
                // Oops, Euclidean algorithm already terminated?
                // 不应该出现这种情况的，因为算法实行过程中余数不能为0
                Log.bomb("r_{i-1} was zero");
            }
            // 辗转相除之后 得到的结果
            r = rLastLast;
            GFPoly q = qrCodeGField.getZero();
            // 分母的首位指数度
            int denominatorLeadingTerm = rLast.getCoefficient(rLast.getDegree());
            // 分母首位指数的倒数
            int dltInverse = qrCodeGField.inverse(denominatorLeadingTerm);
            while (r.getDegree() >= rLast.getDegree() && !r.isZero()) {
                int degreeDiff = r.getDegree() - rLast.getDegree();
                // 计算应该要乘以的指数之间的差值
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

        // 计算得到Σ和Ω
        int inverse = qrCodeGField.inverse(sigmaTildeAtZero);
        GFPoly sigma = t.multiply(inverse);
        GFPoly omega = r.multiply(inverse);
        return new GFPoly[]{sigma, omega};
    }

    /**
     * 找到出错位置
     * @param errorLocator
     * @return
     */
    private int[] findErrorLocations(GFPoly errorLocator) {
        // This is a direct application of Chien's search
        // 使用了一种叫做 Chien's search 的算法
        // 获取错误因数的数量
        int numErrors = errorLocator.getDegree();
        if (numErrors == 1) {
            // shortcut
            // 只有一个出错的情况，捷径路线
            return new int[] { errorLocator.getCoefficient(1) };
        }
        int[] result = new int[numErrors];
        int e = 0;
        for (int i = 1; i < qrCodeGField.getSize() && e < numErrors; i++) {
            // 卧槽，最笨的方法，遍历整个有限域，得到出错位置？？？！！！
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

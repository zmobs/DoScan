/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duqingquan.doscan.qrcode.reedsolomon;


import com.duqingquan.doscan.qrcode.util.Log;

/**
 * 伽罗瓦域的生成多项式
 */
public final class GFPoly {

  /**
   * 伽罗瓦域
   */
  private final QRCodeGField field;
  /**
   * 因数序列
   */
  private final int[] coefficients;

  /**
   * 不仅带限制域还带生成因数数组参数
   * @param field
   * @param coefficients
   */
  GFPoly(QRCodeGField field, int[] coefficients) {
    if (coefficients.length == 0) {
      // 如果选择传递因数，因数数组不能为空
      throw new IllegalArgumentException();
    }
    // 设置有限域参数
    this.field = field;
    int coefficientsLength = coefficients.length;

    if (coefficientsLength > 1 && coefficients[0] == 0) {
      // 等式换算的多项式首位 不能为0
      // Leading term must be non-zero for anything except the constant polynomial "0"
      int firstNonZero = 1;
      while (firstNonZero < coefficientsLength && coefficients[firstNonZero] == 0) {
        firstNonZero++;
      }
      if (firstNonZero == coefficientsLength) {
        this.coefficients = new int[]{0};
      } else {
        this.coefficients = new int[coefficientsLength - firstNonZero];
        System.arraycopy(coefficients,
            firstNonZero,
            this.coefficients,
            0,
            this.coefficients.length);
      }
    } else {
      this.coefficients = coefficients;
    }
  }


  /**
   * @return 返回指定位置的，当前多项式对应的数值
   */
  int evaluateAt(int a) {
    if (a == 0) {
      // 如果是第一个位置，返回x^0的结果
      Log.d("getCoefficient(0)  ---  " + getCoefficient(0));
      return getCoefficient(0);
    }
    if (a == 1) {
      // 如果是第二个位置，位数是1 ，返回系数的和
      // Just the sum of the coefficients
      int result = 0;
      for (int coefficient : coefficients) {
        result = addOrSubtract(result, coefficient);
      }
      return result;
    }
    int result = coefficients[0];
    int size = coefficients.length;
    for (int i = 1; i < size; i++) {
      result = addOrSubtract(field.multiply(a, result), coefficients[i]);
    }
    return result;
  }

  static int addOrSubtract(int a, int b) {
    return a ^ b;
  }


  /**
   * 获取当前指数序列
   * @return 指数
   */
  int[] getCoefficients() {
    return coefficients;
  }

  /**
   * 获取当前多项式深度
   * @return
   */
  int getDegree() {
    // 最后一位系数0 所以-1
    return coefficients.length - 1;
  }

  /**
   * 当前多项式是不是0
   * @return
   */
  boolean isZero() {
    // 判断第一个因数是否为0
    return coefficients[0] == 0;
  }


  /**
   * 获取指定深度的因数
   * @param degree
   * @return
   */
  int getCoefficient(int degree) {
    return coefficients[coefficients.length - 1 - degree];
  }


  /**
   * 多项式内的加减法实现
   * @param other
   * @return
   */
  GFPoly addOrSubtract(GFPoly other) {
    if (!field.equals(other.field)) {
      throw new IllegalArgumentException("GenericGFPolys do not have same QRCodeGField field");
    }
    if (isZero()) {
      return other;
    }
    if (other.isZero()) {
      return this;
    }

    int[] smallerCoefficients = this.coefficients;
    int[] largerCoefficients = other.coefficients;
    if (smallerCoefficients.length > largerCoefficients.length) {
      int[] temp = smallerCoefficients;
      smallerCoefficients = largerCoefficients;
      largerCoefficients = temp;
    }
    int[] sumDiff = new int[largerCoefficients.length];
    int lengthDiff = largerCoefficients.length - smallerCoefficients.length;
    // Copy high-order terms only found in higher-degree polynomial's coefficients
    System.arraycopy(largerCoefficients, 0, sumDiff, 0, lengthDiff);

    for (int i = lengthDiff; i < largerCoefficients.length; i++) {
      sumDiff[i] = QRCodeGField.addOrSubtract(smallerCoefficients[i - lengthDiff], largerCoefficients[i]);
    }

    return new GFPoly(field, sumDiff);
  }

  GFPoly multiply(int scalar) {
    if (scalar == 0) {
      return field.getZero();
    }
    if (scalar == 1) {
      return this;
    }
    int size = coefficients.length;
    int[] product = new int[size];
    for (int i = 0; i < size; i++) {
      product[i] = field.multiply(coefficients[i], scalar);
    }
    return new GFPoly(field, product);
  }


  /**
   * 多项式乘法实现
   * @param other 另外一个多项式
   * @return
   */
  GFPoly multiply(GFPoly other) {

    if (!field.equals(other.field)) {
      throw new IllegalArgumentException("两个相同的多项式不可以一起运算");
    }
    if (isZero() || other.isZero()) {
      return field.getZero();
    }
    // 获取第一个多项式的因数集合
    int[] aCoefficients = this.coefficients;
    int aLength = aCoefficients.length;
    // 获取第二个多项式的因数集合
    int[] bCoefficients = other.coefficients;
    int bLength = bCoefficients.length;
    // 乘积的结果 序列(因为最后的0 只会保存一个，所以需要-1)
    int[] product = new int[aLength + bLength - 1];
    // 计算乘积各项因数并且填充
    for (int i = 0; i < aLength; i++) {
      int aCoeff = aCoefficients[i];
      for (int j = 0; j < bLength; j++) {
        product[i + j] = QRCodeGField.addOrSubtract(product[i + j],
            field.multiply(aCoeff, bCoefficients[j]));
      }
    }
    // 得到当前深度情况下的生成多项式公式
    return new GFPoly(field, product);
  }

//
//  GFPoly multiply(int scalar) {
//    if (scalar == 0) {
//      return field.getZero();
//    }
//    if (scalar == 1) {
//      return this;
//    }
//    int size = coefficients.length;
//    int[] product = new int[size];
//    for (int i = 0; i < size; i++) {
//      product[i] = field.multiply(coefficients[i], scalar);
//    }
//    return new GFPoly(field, product);
//  }
//

  GFPoly multiplyByMonomial(int degree, int coefficient) {
    if (degree < 0) {
      throw new IllegalArgumentException();
    }
    if (coefficient == 0) {
      return field.getZero();
    }
    int size = coefficients.length;
    int[] product = new int[size + degree];
    for (int i = 0; i < size; i++) {
      product[i] = field.multiply(coefficients[i], coefficient);
    }
    return new GFPoly(field, product);
  }

  /**
   * 多项式除法实现
   * @param other
   * @return
   */
  GFPoly[] divide(GFPoly other) {
    if (!field.equals(other.field)) {
      throw new IllegalArgumentException("多项式不能自己收拾自己");
    }
    if (other.isZero()) {
      throw new IllegalArgumentException("除数为0");
    }

    GFPoly quotient = field.getZero();
    GFPoly remainder = this;

    int denominatorLeadingTerm = other.getCoefficient(other.getDegree());
    int inverseDenominatorLeadingTerm = field.inverse(denominatorLeadingTerm);

    while (remainder.getDegree() >= other.getDegree() && !remainder.isZero()) {
      int degreeDifference = remainder.getDegree() - other.getDegree();
      int scale = field.multiply(remainder.getCoefficient(remainder.getDegree()), inverseDenominatorLeadingTerm);
      GFPoly term = other.multiplyByMonomial(degreeDifference, scale);
      GFPoly iterationQuotient = field.buildMonomial(degreeDifference, scale);
      quotient = quotient.addOrSubtract(iterationQuotient);
      remainder = remainder.addOrSubtract(term);
    }

    return new GFPoly[] { quotient, remainder };
  }


  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(8 * getDegree());
    for (int degree = getDegree(); degree >= 0; degree--) {
      int coefficient = getCoefficient(degree);
      if (coefficient != 0) {
        if (coefficient < 0) {
          result.append(" - ");
          coefficient = -coefficient;
        } else {
          if (result.length() > 0) {
            result.append(" + ");
          }
        }
        if (degree == 0 || coefficient != 1) {
          int alphaPower = field.log(coefficient);
          if (alphaPower == 0) {
            result.append('1');
          } else if (alphaPower == 1) {
            result.append('a');
          } else {
            result.append("a^");
            result.append(alphaPower);
          }
        }
        if (degree != 0) {
          if (degree == 1) {
            result.append('x');
          } else {
            result.append("x^");
            result.append(degree);
          }
        }
      }
    }
    return result.toString();
  }

}

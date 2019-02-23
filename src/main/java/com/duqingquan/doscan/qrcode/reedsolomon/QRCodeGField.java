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

/**
 * 二维码的伽罗瓦域定义
 */
public final class QRCodeGField {


  /**
   * 字节取模运算基数（1000011101）
   */
  private final int BYTE_MOD_BASE = 0x011D;
  /**
   * 比特取模运算基数
   */
  private final int BIT_MOD_BASE = 2;
  /**
   * GF 域大小为256
   */
  private final int FIELD_SIZE = 256;
  /**
   * 生成多项式基数的因数   这里是0
   */
  public final int GENERATOR_BASE = 0;

  // 多项式指数序列
  private final int[] expTable = new int[FIELD_SIZE];
  // 多项式对数序列
  private final int[] logTable = new int[FIELD_SIZE];

  public int getSize(){
    return 256;
  }

  private final GFPoly zero;
  private final GFPoly one;


  /**
   * Create a representation of GF(size) using the given primitive polynomial.
   *
   * @param primitive irreducible polynomial whose coefficients are represented by
   *  the bits of an int, where the least-significant bit represents the constant
   *  coefficient
   * @param size the size of the field
   * @param b the factor b in the generator polynomial can be 0- or 1-based
   *  (g(x) = (x+a^b)(x+a^(b+1))...(x+a^(b+2t-1))).
   *  In most cases it should be 1, but for QR code it is 0.
   */
  public QRCodeGField() {

    int x = 1;
    for (int i = 0; i < FIELD_SIZE; i++) {
      expTable[i] = x;
      x *= 2; // 生成器的系数是2
      if (x >= FIELD_SIZE) {
        x ^= BYTE_MOD_BASE;
        x &= FIELD_SIZE - 1;
      }
    }
    for (int i = 0; i < FIELD_SIZE - 1; i++) {
      // 计算指数表的倒数，并储存至数组
      logTable[expTable[i]] = i;
    }
    // logTable[0] == 0 but this should never be used yes, that is right
    zero = new GFPoly(this, new int[]{0});
    one = new GFPoly(this, new int[]{1});
  }

  /**
   * 返回单项式0
   * @return 单项式0的  伽罗瓦域表示
   */
  GFPoly getZero() {
    return zero;
  }

  GFPoly getOne() {
    return one;
  }

  /**
   * 用单项式来代表  因数^x^系数
   * @param degree
   * @param coefficient
   * @return
   */
  GFPoly buildMonomial(int degree, int coefficient) {
    if (degree < 0) {
      throw new IllegalArgumentException();
    }
    if (coefficient == 0) {
      return zero;
    }
    int[] coefficients = new int[degree + 1];
    coefficients[0] = coefficient;
    return new GFPoly(this, coefficients);
  }
//
//  /**
//   * Implements both addition and subtraction -- they are the same in GF(size).
//   *
//   * @return sum/difference of a and b
//   */

  /**
   * 加减法实现，在有限域种都是异或运算，所以不区分（别问我为啥，我特么也没看懂）
   *
   * @param a
   * @param b
   * @return
   */
  static int addOrSubtract(int a, int b) {
    return a ^ b;
  }

  /**
   * 返回指定位置的 数字 2的指数值
   * @param a
   * @return
   */
  int exp(int a) {
    return expTable[a];
  }

  /**
   * 获取指定位置的系数
   * @param a
   * @return
   */
  int log(int a) {
    if (a == 0) {
      throw new IllegalArgumentException();
    }
    return logTable[a];
  }

  /**
   * 对某个数字进行反转，其实就是最大值相减
   * @param a
   * @return
   */
  int inverse(int a) {
    if (a == 0) {
      throw new ArithmeticException();
    }
    return expTable[FIELD_SIZE - logTable[a] - 1];
  }

  /**
   * 有限域内的多项式乘法，因数相加然后 取模最大尺寸
   */
  int multiply(int a, int b) {
    if (a == 0 || b == 0) {
      return 0;
    }
    //Log.d("1111 a --- " + a);
    return expTable[(logTable[a] + logTable[b]) % (FIELD_SIZE - 1)];
  }
//
//  public int getSize() {
//    return size;
//  }
//
//  public int getGeneratorBase() {
//    return generatorBase;
//  }
//
//  @Override
//  public String toString() {
//    return "GF(0x" + Integer.toHexString(primitive) + ',' + size + ')';
//  }
//
}

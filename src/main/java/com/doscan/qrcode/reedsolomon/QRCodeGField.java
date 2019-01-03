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

package com.doscan.qrcode.reedsolomon;

import com.doscan.qrcode.util.Log;

/**
 * <p>This class contains utility methods for performing mathematical operations over
 * the Galois Fields. Operations use a given primitive polynomial in calculations.</p>
 *
 * <p>Throughout this package, elements of the GF are represented as an {@code int}
 * for convenience and speed (but at the cost of memory).
 * </p>
 *
 * @author Sean Owen
 * @author David Olivier
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
   * 生成器基数   这里是0
   */
  private final int GENERATOR_BASE = 0;

  // 多项式指数序列
  private final int[] expTable = new int[FIELD_SIZE];
  // 多项式对数序列
  private final int[] logTable = new int[FIELD_SIZE];



//  private final GFPoly zero;
//  private final GFPoly one;


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
      x *= 2; // we're assuming the generator alpha is 2
      if (x >= FIELD_SIZE) {
        x ^= BYTE_MOD_BASE;
        x &= FIELD_SIZE - 1;
      }
      Log.d(" i ----  " + i + "   x  ---  " + x);
    }
    for (int i = 0; i < FIELD_SIZE - 1; i++) {
      logTable[expTable[i]] = i;
    }
//    // logTable[0] == 0 but this should never be used
//    zero = new GFPoly(this, new int[]{0});
//    one = new GFPoly(this, new int[]{1});
  }

//  GFPoly getZero() {
//    return zero;
//  }
//
//  GFPoly getOne() {
//    return one;
//  }
//
//  /**
//   * @return the monomial representing coefficient * x^degree
//   */
//  GFPoly buildMonomial(int degree, int coefficient) {
//    if (degree < 0) {
//      throw new IllegalArgumentException();
//    }
//    if (coefficient == 0) {
//      return zero;
//    }
//    int[] coefficients = new int[degree + 1];
//    coefficients[0] = coefficient;
//    return new GFPoly(this, coefficients);
//  }
//
//  /**
//   * Implements both addition and subtraction -- they are the same in GF(size).
//   *
//   * @return sum/difference of a and b
//   */
//  static int addOrSubtract(int a, int b) {
//    return a ^ b;
//  }
//
//  /**
//   * @return 2 to the power of a in GF(size)
//   */
//  int exp(int a) {
//    return expTable[a];
//  }
//
//  /**
//   * @return base 2 log of a in GF(size)
//   */
//  int log(int a) {
//    if (a == 0) {
//      throw new IllegalArgumentException();
//    }
//    return logTable[a];
//  }
//
//  /**
//   * @return multiplicative inverse of a
//   */
//  int inverse(int a) {
//    if (a == 0) {
//      throw new ArithmeticException();
//    }
//    return expTable[size - logTable[a] - 1];
//  }
//
//  /**
//   * @return product of a and b in GF(size)
//   */
//  int multiply(int a, int b) {
//    if (a == 0 || b == 0) {
//      return 0;
//    }
//    return expTable[(logTable[a] + logTable[b]) % (size - 1)];
//  }
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

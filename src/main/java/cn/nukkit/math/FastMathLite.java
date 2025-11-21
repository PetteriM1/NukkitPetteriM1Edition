/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.nukkit.math;

/**
 * Faster, more accurate, portable alternative to {@link Math} and
 * {@link StrictMath} for large scale computation.
 * <p>
 * This class contains FastMath methods we use from Apache Commons Math 3.6.1.
 */
public class FastMathLite {

    private static final double[] SINE_TABLE_A =
            {
                    +0.0d,
                    +0.1246747374534607d,
                    +0.24740394949913025d,
                    +0.366272509098053d,
                    +0.4794255495071411d,
                    +0.5850973129272461d,
                    +0.6816387176513672d,
                    +0.7675435543060303d,
                    +0.8414709568023682d,
                    +0.902267575263977d,
                    +0.9489846229553223d,
                    +0.9808930158615112d,
                    +0.9974949359893799d,
                    +0.9985313415527344d,
            };

    private static final double[] SINE_TABLE_B =
            {
                    +0.0d,
                    -4.068233003401932E-9d,
                    +9.755392680573412E-9d,
                    +1.9987994582857286E-8d,
                    -1.0902938113007961E-8d,
                    -3.9986783938944604E-8d,
                    +4.23719669792332E-8d,
                    -5.207000323380292E-8d,
                    +2.800552834259E-8d,
                    +1.883511811213715E-8d,
                    -3.5997360512765566E-9d,
                    +4.116164446561962E-8d,
                    +5.0614674548127384E-8d,
                    -1.0129027912496858E-9d,
            };

    private static final double[] COSINE_TABLE_A =
            {
                    +1.0d,
                    +0.9921976327896118d,
                    +0.9689123630523682d,
                    +0.9305076599121094d,
                    +0.8775825500488281d,
                    +0.8109631538391113d,
                    +0.7316888570785522d,
                    +0.6409968137741089d,
                    +0.5403022766113281d,
                    +0.4311765432357788d,
                    +0.3153223395347595d,
                    +0.19454771280288696d,
                    +0.07073719799518585d,
                    -0.05417713522911072d,
            };

    private static final double[] COSINE_TABLE_B =
            {
                    +0.0d,
                    +3.4439717236742845E-8d,
                    +5.865827662008209E-8d,
                    -3.7999795083850525E-8d,
                    +1.184154459111628E-8d,
                    -3.43338934259355E-8d,
                    +1.1795268640216787E-8d,
                    +4.438921624363781E-8d,
                    +2.925681159240093E-8d,
                    -2.6437112632041807E-8d,
                    +2.2860509143963117E-8d,
                    -4.813899778443457E-9d,
                    +3.6725170580355583E-9d,
                    +2.0217439756338078E-10d,
            };


    private static final double[] TANGENT_TABLE_A =
            {
                    +0.0d,
                    +0.1256551444530487d,
                    +0.25534194707870483d,
                    +0.3936265707015991d,
                    +0.5463024377822876d,
                    +0.7214844226837158d,
                    +0.9315965175628662d,
                    +1.1974215507507324d,
                    +1.5574076175689697d,
                    +2.092571258544922d,
                    +3.0095696449279785d,
                    +5.041914939880371d,
                    +14.101419448852539d,
                    -18.430862426757812d,
            };

    private static final double[] TANGENT_TABLE_B =
            {
                    +0.0d,
                    -7.877917738262007E-9d,
                    -2.5857668567479893E-8d,
                    +5.2240336371356666E-9d,
                    +5.206150291559893E-8d,
                    +1.8307188599677033E-8d,
                    -5.7618793749770706E-8d,
                    +7.848361555046424E-8d,
                    +1.0708593250394448E-7d,
                    +1.7827257129423813E-8d,
                    +2.893485277253286E-8d,
                    +3.1660099222737955E-7d,
                    +4.983191803254889E-7d,
                    -3.356118100840571E-7d,
            };

    private static final long[] RECIP_2PI = new long[]{
            (0x28be60dbL << 32) | 0x9391054aL,
            (0x7f09d5f4L << 32) | 0x7d4d3770L,
            (0x36d8a566L << 32) | 0x4f10e410L,
            (0x7f9458eaL << 32) | 0xf7aef158L,
            (0x6dc91b8eL << 32) | 0x909374b8L,
            (0x01924bbaL << 32) | 0x82746487L,
            (0x3f877ac7L << 32) | 0x2c4a69cfL,
            (0xba208d7dL << 32) | 0x4baed121L,
            (0x3a671c09L << 32) | 0xad17df90L,
            (0x4e64758eL << 32) | 0x60d4ce7dL,
            (0x272117e2L << 32) | 0xef7e4a0eL,
            (0xc7fe25ffL << 32) | 0xf7816603L,
            (0xfbcbc462L << 32) | 0xd6829b47L,
            (0xdb4d9fb3L << 32) | 0xc9f2c26dL,
            (0xd3d18fd9L << 32) | 0xa797fa8bL,
            (0x5d49eeb1L << 32) | 0xfaf97c5eL,
            (0xcf41ce7dL << 32) | 0xe294a4baL,
            0x9afed7ecL << 32};

    private static final long[] PI_O_4_BITS = new long[]{
            (0xc90fdaa2L << 32) | 0x2168c234L,
            (0xc4c6628bL << 32) | 0x80dc1cd1L};

    private static final double[] EIGHTHS = {0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0, 1.125, 1.25, 1.375, 1.5, 1.625};

    private static final double[] CBRTTWO = {0.6299605249474366,
            0.7937005259840998,
            1.0,
            1.2599210498948732,
            1.5874010519681994};


    private static final long HEX_40000000 = 0x40000000L;
    private static final long MASK_30BITS = -1L - (HEX_40000000 - 1);
    private static final int MASK_NON_SIGN_INT = 0x7fffffff;

    private static final long MASK_NON_SIGN_LONG = 0x7fffffffffffffffl;

    private static final double TWO_POWER_52 = 4503599627370496.0;

    private static final double F_3_4 = 3d / 4d;
    private static final double F_1_2 = 1d / 2d;
    private static final double F_1_4 = 1d / 4d;

    private static final double SAFE_MIN;
    private static final long EXPONENT_OFFSET = 1023l;

    static {
        SAFE_MIN = Double.longBitsToDouble((EXPONENT_OFFSET - 1022l) << 52);
    }

    private FastMathLite() {
    }

    private static class Split {

        public static final Split NAN = new Split(Double.NaN, 0);

        public static final Split POSITIVE_INFINITY = new Split(Double.POSITIVE_INFINITY, 0);

        public static final Split NEGATIVE_INFINITY = new Split(Double.NEGATIVE_INFINITY, 0);

        private final double full;

        private final double high;

        private final double low;

        Split(final double x) {
            full = x;
            high = Double.longBitsToDouble(Double.doubleToRawLongBits(x) & ((-1L) << 27));
            low = x - high;
        }

        Split(final double high, final double low) {
            this(high == 0.0 ? (low == 0.0 && Double.doubleToRawLongBits(high) == Long.MIN_VALUE /* negative zero */ ? -0.0 : low) : high + low, high, low);
        }

        Split(final double full, final double high, final double low) {
            this.full = full;
            this.high = high;
            this.low = low;
        }

        public Split multiply(final Split b) {
            final Split mulBasic = new Split(full * b.full);
            final double mulError = low * b.low - (((mulBasic.full - high * b.high) - low * b.high) - high * b.low);
            return new Split(mulBasic.high, mulBasic.low + mulError);
        }

        private Split pow(final long e) {

            Split result = new Split(1);

            Split d2p = new Split(full, high, low);

            for (long p = e; p != 0; p >>>= 1) {

                if ((p & 0x1) != 0) {
                    result = result.multiply(d2p);
                }

                d2p = d2p.multiply(d2p);

            }

            if (Double.isNaN(result.full)) {
                if (Double.isNaN(full)) {
                    return Split.NAN;
                } else {
                    if (FastMathLite.abs(full) < 1) {
                        return new Split(FastMathLite.copySign(0.0, full), 0.0);
                    } else if (full < 0 && (e & 0x1) == 1) {
                        return Split.NEGATIVE_INFINITY;
                    } else {
                        return Split.POSITIVE_INFINITY;
                    }
                }
            } else {
                return result;
            }

        }

        public Split reciprocal() {

            final double approximateInv = 1.0 / full;
            final Split splitInv = new Split(approximateInv);

            final Split product = multiply(splitInv);
            final double error = (product.high - 1) + product.low;

            return Double.isNaN(error) ? splitInv : new Split(splitInv.high, splitInv.low - error / full);

        }

    }

    private static class CodyWaite {
        private final int finalK;
        private final double finalRemA;
        private final double finalRemB;

        CodyWaite(double xa) {
            int k = (int) (xa * 0.6366197723675814);

            double remA;
            double remB;
            while (true) {
                double a = -k * 1.570796251296997;
                remA = xa + a;
                remB = -(remA - xa - a);

                a = -k * 7.549789948768648E-8;
                double b = remA;
                remA = a + b;
                remB += -(remA - b - a);

                a = -k * 6.123233995736766E-17;
                b = remA;
                remA = a + b;
                remB += -(remA - b - a);

                if (remA > 0) {
                    break;
                }

                --k;
            }

            this.finalK = k;
            this.finalRemA = remA;
            this.finalRemB = remB;
        }

        int getK() {
            return finalK;
        }

        double getRemA() {
            return finalRemA;
        }

        double getRemB() {
            return finalRemB;
        }
    }

    public static double IEEEremainder(double dividend, double divisor) {
        return StrictMath.IEEEremainder(dividend, divisor);
    }

    public static int abs(final int x) {
        final int i = x >>> 31;
        return (x ^ (~i + 1)) + i;
    }

    public static long abs(final long x) {
        final long l = x >>> 63;
        return (x ^ (~l + 1)) + l;
    }

    public static float abs(final float x) {
        return Float.intBitsToFloat(MASK_NON_SIGN_INT & Float.floatToRawIntBits(x));
    }

    public static double abs(double x) {
        return Double.longBitsToDouble(MASK_NON_SIGN_LONG & Double.doubleToRawLongBits(x));
    }

    public static double acos(double x) {
        if (x != x) {
            return Double.NaN;
        }

        if (x > 1.0 || x < -1.0) {
            return Double.NaN;
        }

        if (x == -1.0) {
            return Math.PI;
        }

        if (x == 1.0) {
            return 0.0;
        }

        if (x == 0) {
            return Math.PI / 2.0;
        }


        double temp = x * HEX_40000000;
        final double xa = x + temp - temp;
        final double xb = x - xa;

        double ya = xa * xa;
        double yb = xa * xb * 2.0 + xb * xb;

        ya = -ya;
        yb = -yb;

        double za = 1.0 + ya;
        double zb = -(za - 1.0 - ya);

        temp = za + yb;
        zb += -(temp - za - yb);
        za = temp;

        double y = sqrt(za);
        temp = y * HEX_40000000;
        ya = y + temp - temp;
        yb = y - ya;

        yb += (za - ya * ya - 2 * ya * yb - yb * yb) / (2.0 * y);

        yb += zb / (2.0 * y);
        y = ya + yb;
        yb = -(y - ya - yb);

        double r = y / x;

        if (Double.isInfinite(r)) {
            return Math.PI / 2;
        }

        double ra = doubleHighPart(r);
        double rb = r - ra;

        rb += (y - ra * xa - ra * xb - rb * xa - rb * xb) / x;
        rb += yb / x;
        temp = ra + rb;
        rb = -(temp - ra - rb);
        ra = temp;

        return atan(ra, rb, x < 0);
    }

    public static int addExact(final int a, final int b) throws RuntimeException {

        final int sum = a + b;

        if ((a ^ b) >= 0 && (sum ^ b) < 0) {
            throw new RuntimeException("OVERFLOW_IN_ADDITION");
        }

        return sum;

    }

    public static long addExact(final long a, final long b) throws RuntimeException {

        final long sum = a + b;

        if ((a ^ b) >= 0 && (sum ^ b) < 0) {
            throw new RuntimeException("OVERFLOW_IN_ADDITION");
        }

        return sum;

    }

    public static double asin(double x) {
        if (x != x) {
            return Double.NaN;
        }

        if (x > 1.0 || x < -1.0) {
            return Double.NaN;
        }

        if (x == 1.0) {
            return Math.PI / 2.0;
        }

        if (x == -1.0) {
            return -Math.PI / 2.0;
        }

        if (x == 0.0) {
            return x;
        }


        double temp = x * HEX_40000000;
        final double xa = x + temp - temp;
        final double xb = x - xa;

        double ya = xa * xa;
        double yb = xa * xb * 2.0 + xb * xb;

        ya = -ya;
        yb = -yb;

        double za = 1.0 + ya;
        double zb = -(za - 1.0 - ya);

        temp = za + yb;
        zb += -(temp - za - yb);
        za = temp;

        double y;
        y = sqrt(za);
        temp = y * HEX_40000000;
        ya = y + temp - temp;
        yb = y - ya;

        yb += (za - ya * ya - 2 * ya * yb - yb * yb) / (2.0 * y);

        double dx = zb / (2.0 * y);

        double r = x / y;
        temp = r * HEX_40000000;
        double ra = r + temp - temp;
        double rb = r - ra;

        rb += (x - ra * ya - ra * yb - rb * ya - rb * yb) / y;
        rb += -x * dx / y / y;
        temp = ra + rb;
        rb = -(temp - ra - rb);
        ra = temp;

        return atan(ra, rb, false);
    }

    public static double atan(double x) {
        return atan(x, 0.0, false);
    }

    private static double atan(double xa, double xb, boolean leftPlane) {
        if (xa == 0.0) {
            return leftPlane ? copySign(Math.PI, xa) : xa;
        }

        final boolean negate;
        if (xa < 0) {
            xa = -xa;
            xb = -xb;
            negate = true;
        } else {
            negate = false;
        }

        if (xa > 1.633123935319537E16) {
            return (negate ^ leftPlane) ? (-Math.PI * F_1_2) : (Math.PI * F_1_2);
        }

        final int idx;
        if (xa < 1) {
            idx = (int) (((-1.7168146928204136 * xa * xa + 8.0) * xa) + 0.5);
        } else {
            final double oneOverXa = 1 / xa;
            idx = (int) (-((-1.7168146928204136 * oneOverXa * oneOverXa + 8.0) * oneOverXa) + 13.07);
        }

        final double ttA = TANGENT_TABLE_A[idx];
        final double ttB = TANGENT_TABLE_B[idx];

        double epsA = xa - ttA;
        double epsB = -(epsA - xa + ttA);
        epsB += xb - ttB;

        double temp = epsA + epsB;
        epsB = -(temp - epsA - epsB);
        epsA = temp;

        temp = xa * HEX_40000000;
        double ya = xa + temp - temp;
        double yb = xb + xa - ya;
        xa = ya;
        xb += yb;

        if (idx == 0) {
            final double denom = 1d / (1d + (xa + xb) * (ttA + ttB));
            ya = epsA * denom;
            yb = epsB * denom;
        } else {
            double temp2 = xa * ttA;
            double za = 1d + temp2;
            double zb = -(za - 1d - temp2);
            temp2 = xb * ttA + xa * ttB;
            temp = za + temp2;
            zb += -(temp - za - temp2);
            za = temp;

            zb += xb * ttB;
            ya = epsA / za;

            temp = ya * HEX_40000000;
            final double yaa = (ya + temp) - temp;
            final double yab = ya - yaa;

            temp = za * HEX_40000000;
            final double zaa = (za + temp) - temp;
            final double zab = za - zaa;

            yb = (epsA - yaa * zaa - yaa * zab - yab * zaa - yab * zab) / za;

            yb += -epsA * zb / za / za;
            yb += epsB / za;
        }


        epsA = ya;
        epsB = yb;

        final double epsA2 = epsA * epsA;


        yb = 0.07490822288864472;
        yb = yb * epsA2 - 0.09088450866185192;
        yb = yb * epsA2 + 0.11111095942313305;
        yb = yb * epsA2 - 0.1428571423679182;
        yb = yb * epsA2 + 0.19999999999923582;
        yb = yb * epsA2 - 0.33333333333333287;
        yb = yb * epsA2 * epsA;


        ya = epsA;

        temp = ya + yb;
        yb = -(temp - ya - yb);
        ya = temp;

        yb += epsB / (1d + epsA * epsA);

        final double eighths = EIGHTHS[idx];

        double za = eighths + ya;
        double zb = -(za - eighths - ya);
        temp = za + yb;
        zb += -(temp - za - yb);
        za = temp;

        double result = za + zb;

        if (leftPlane) {
            final double resultb = -(result - za - zb);
            final double pia = 1.5707963267948966 * 2;
            final double pib = 6.123233995736766E-17 * 2;

            za = pia - result;
            zb = -(za - pia + result);
            zb += pib - resultb;

            result = za + zb;
        }


        if (negate ^ leftPlane) {
            result = -result;
        }

        return result;
    }

    public static double atan2(double y, double x) {
        if (x != x || y != y) {
            return Double.NaN;
        }

        if (y == 0) {
            final double result = x * y;
            final double invx = 1d / x;
            final double invy = 1d / y;

            if (invx == 0) {
                if (x > 0) {
                    return y;
                } else {
                    return copySign(Math.PI, y);
                }
            }

            if (x < 0 || invx < 0) {
                if (y < 0 || invy < 0) {
                    return -Math.PI;
                } else {
                    return Math.PI;
                }
            } else {
                return result;
            }
        }


        if (y == Double.POSITIVE_INFINITY) {
            if (x == Double.POSITIVE_INFINITY) {
                return Math.PI * F_1_4;
            }

            if (x == Double.NEGATIVE_INFINITY) {
                return Math.PI * F_3_4;
            }

            return Math.PI * F_1_2;
        }

        if (y == Double.NEGATIVE_INFINITY) {
            if (x == Double.POSITIVE_INFINITY) {
                return -Math.PI * F_1_4;
            }

            if (x == Double.NEGATIVE_INFINITY) {
                return -Math.PI * F_3_4;
            }

            return -Math.PI * F_1_2;
        }

        if (x == Double.POSITIVE_INFINITY) {
            if (y > 0 || 1 / y > 0) {
                return 0d;
            }

            if (y < 0 || 1 / y < 0) {
                return -0d;
            }
        }

        if (x == Double.NEGATIVE_INFINITY) {
            if (y > 0.0 || 1 / y > 0.0) {
                return Math.PI;
            }

            if (y < 0 || 1 / y < 0) {
                return -Math.PI;
            }
        }


        if (x == 0) {
            if (y > 0 || 1 / y > 0) {
                return Math.PI * F_1_2;
            }

            if (y < 0 || 1 / y < 0) {
                return -Math.PI * F_1_2;
            }
        }

        final double r = y / x;
        if (Double.isInfinite(r)) {
            return atan(r, 0, x < 0);
        }

        double ra = doubleHighPart(r);
        double rb = r - ra;

        final double xa = doubleHighPart(x);
        final double xb = x - xa;

        rb += (y - ra * xa - ra * xb - rb * xa - rb * xb) / x;

        final double temp = ra + rb;
        rb = -(temp - ra - rb);
        ra = temp;

        if (ra == 0) {
            ra = copySign(0d, y);
        }

        return atan(ra, rb, x < 0);
    }

    public static double cbrt(double x) {
        long inbits = Double.doubleToRawLongBits(x);
        int exponent = (int) ((inbits >> 52) & 0x7ff) - 1023;
        boolean subnormal = false;

        if (exponent == -1023) {
            if (x == 0) {
                return x;
            }

            subnormal = true;
            x *= 1.8014398509481984E16;
            inbits = Double.doubleToRawLongBits(x);
            exponent = (int) ((inbits >> 52) & 0x7ff) - 1023;
        }

        if (exponent == 1024) {
            return x;
        }

        int exp3 = exponent / 3;

        double p2 = Double.longBitsToDouble((inbits & 0x8000000000000000L) |
                (long) (((exp3 + 1023) & 0x7ff)) << 52);

        final double mant = Double.longBitsToDouble((inbits & 0x000fffffffffffffL) | 0x3ff0000000000000L);

        double est = -0.010714690733195933;
        est = est * mant + 0.0875862700108075;
        est = est * mant + -0.3058015757857271;
        est = est * mant + 0.7249995199969751;
        est = est * mant + 0.5039018405998233;

        est *= CBRTTWO[exponent % 3 + 2];

        final double xs = x / (p2 * p2 * p2);
        est += (xs - est * est * est) / (3 * est * est);
        est += (xs - est * est * est) / (3 * est * est);

        double temp = est * HEX_40000000;
        double ya = est + temp - temp;
        double yb = est - ya;

        double za = ya * ya;
        double zb = ya * yb * 2.0 + yb * yb;
        temp = za * HEX_40000000;
        double temp2 = za + temp - temp;
        zb += za - temp2;
        za = temp2;

        zb = za * yb + ya * zb + zb * yb;
        za *= ya;

        double na = xs - za;
        double nb = -(na - xs + za);
        nb -= zb;

        est += (na + nb) / (3 * est * est);

        est *= p2;

        if (subnormal) {
            est *= 3.814697265625E-6;
        }

        return est;
    }

    public static double ceil(double x) {
        double y;

        if (x != x) {
            return x;
        }

        y = floor(x);
        if (y == x) {
            return y;
        }

        y += 1.0;

        if (y == 0) {
            return x * y;
        }

        return y;
    }

    public static double copySign(double magnitude, double sign) {
        final long m = Double.doubleToRawLongBits(magnitude);
        final long s = Double.doubleToRawLongBits(sign);
        if ((m ^ s) >= 0) {
            return magnitude;
        }
        return -magnitude;
    }

    public static float copySign(float magnitude, float sign) {
        final int m = Float.floatToRawIntBits(magnitude);
        final int s = Float.floatToRawIntBits(sign);
        if ((m ^ s) >= 0) {
            return magnitude;
        }
        return -magnitude;
    }

    public static double cos(double x) {
        int quadrant = 0;

        double xa = x;
        if (x < 0) {
            xa = -xa;
        }

        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }

        double xb = 0;
        if (xa > 3294198.0) {
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            final CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }


        switch (quadrant) {
            case 0:
                return cosQ(xa, xb);
            case 1:
                return -sinQ(xa, xb);
            case 2:
                return -cosQ(xa, xb);
            case 3:
                return sinQ(xa, xb);
            default:
                return Double.NaN;
        }
    }

    private static double cosQ(double xa, double xb) {
        final double pi2a = 1.5707963267948966;
        final double pi2b = 6.123233995736766E-17;

        final double a = pi2a - xa;
        double b = -(a - pi2a + xa);
        b += pi2b - xb;

        return sinQ(a, b);
    }

    public static int decrementExact(final int n) throws RuntimeException {

        if (n == Integer.MIN_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_SUBTRACTION");
        }

        return n - 1;

    }

    public static long decrementExact(final long n) throws RuntimeException {

        if (n == Long.MIN_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_SUBTRACTION");
        }

        return n - 1;

    }

    private static double doubleHighPart(double d) {
        if (d > -/*Precision.*/SAFE_MIN && d < /*Precision.*/SAFE_MIN) {
            return d;
        }
        long xl = Double.doubleToRawLongBits(d);
        xl &= MASK_30BITS;
        return Double.longBitsToDouble(xl);
    }

    public static double floor(double x) {
        long y;

        if (x != x) {
            return x;
        }

        if (x >= TWO_POWER_52 || x <= -TWO_POWER_52) {
            return x;
        }

        y = (long) x;
        if (x < 0 && y != x) {
            y--;
        }

        if (y == 0) {
            return x * y;
        }

        return y;
    }

    public static int floorDiv(final int a, final int b) throws RuntimeException {

        if (b == 0) {
            throw new RuntimeException("ZERO_DENOMINATOR");
        }

        final int m = a % b;
        if ((a ^ b) >= 0 || m == 0) {
            return a / b;
        } else {
            return (a / b) - 1;
        }

    }

    public static long floorDiv(final long a, final long b) throws RuntimeException {

        if (b == 0l) {
            throw new RuntimeException("ZERO_DENOMINATOR");
        }

        final long m = a % b;
        if ((a ^ b) >= 0l || m == 0l) {
            return a / b;
        } else {
            return (a / b) - 1l;
        }

    }

    public static int floorMod(final int a, final int b) throws RuntimeException {

        if (b == 0) {
            throw new RuntimeException("ZERO_DENOMINATOR");
        }

        final int m = a % b;
        if ((a ^ b) >= 0 || m == 0) {
            return m;
        } else {
            return b + m;
        }

    }

    public static long floorMod(final long a, final long b) {

        if (b == 0l) {
            throw new RuntimeException("ZERO_DENOMINATOR");
        }

        final long m = a % b;
        if ((a ^ b) >= 0l || m == 0l) {
            return m;
        } else {
            return b + m;
        }

    }

    public static int getExponent(final double d) {
        return (int) ((Double.doubleToRawLongBits(d) >>> 52) & 0x7ff) - 1023;
    }

    public static int getExponent(final float f) {
        return ((Float.floatToRawIntBits(f) >>> 23) & 0xff) - 127;
    }

    public static double hypot(final double x, final double y) {
        if (Double.isInfinite(x) || Double.isInfinite(y)) {
            return Double.POSITIVE_INFINITY;
        } else if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        } else {

            final int expX = getExponent(x);
            final int expY = getExponent(y);
            if (expX > expY + 27) {
                return abs(x);
            } else if (expY > expX + 27) {
                return abs(y);
            } else {

                final int middleExp = (expX + expY) / 2;

                final double scaledX = scalb(x, -middleExp);
                final double scaledY = scalb(y, -middleExp);

                final double scaledH = sqrt(scaledX * scaledX + scaledY * scaledY);

                return scalb(scaledH, middleExp);

            }

        }
    }

    public static int incrementExact(final int n) throws RuntimeException {

        if (n == Integer.MAX_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_ADDITION");
        }

        return n + 1;

    }

    public static long incrementExact(final long n) throws RuntimeException {

        if (n == Long.MAX_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_ADDITION");
        }

        return n + 1;

    }

    public static int max(final int a, final int b) {
        return (a <= b) ? b : a;
    }

    public static long max(final long a, final long b) {
        return (a <= b) ? b : a;
    }

    public static float max(final float a, final float b) {
        if (a > b) {
            return a;
        }
        if (a < b) {
            return b;
        }
        if (a != b) {
            return Float.NaN;
        }
        int bits = Float.floatToRawIntBits(a);
        if (bits == 0x80000000) {
            return b;
        }
        return a;
    }

    public static double max(final double a, final double b) {
        if (a > b) {
            return a;
        }
        if (a < b) {
            return b;
        }
        if (a != b) {
            return Double.NaN;
        }
        long bits = Double.doubleToRawLongBits(a);
        if (bits == 0x8000000000000000L) {
            return b;
        }
        return a;
    }

    public static int min(final int a, final int b) {
        return (a <= b) ? a : b;
    }

    public static long min(final long a, final long b) {
        return (a <= b) ? a : b;
    }

    public static float min(final float a, final float b) {
        if (a > b) {
            return b;
        }
        if (a < b) {
            return a;
        }
        if (a != b) {
            return Float.NaN;
        }
        int bits = Float.floatToRawIntBits(a);
        if (bits == 0x80000000) {
            return a;
        }
        return b;
    }

    public static double min(final double a, final double b) {
        if (a > b) {
            return b;
        }
        if (a < b) {
            return a;
        }
        if (a != b) {
            return Double.NaN;
        }
        long bits = Double.doubleToRawLongBits(a);
        if (bits == 0x8000000000000000L) {
            return a;
        }
        return b;
    }

    public static int multiplyExact(final int a, final int b) {
        if (((b > 0) && (a > Integer.MAX_VALUE / b || a < Integer.MIN_VALUE / b)) ||
                ((b < -1) && (a > Integer.MIN_VALUE / b || a < Integer.MAX_VALUE / b)) ||
                ((b == -1) && (a == Integer.MIN_VALUE))) {
            throw new RuntimeException("OVERFLOW_IN_MULTIPLICATION");
        }
        return a * b;
    }

    public static long multiplyExact(final long a, final long b) {
        if (((b > 0l) && (a > Long.MAX_VALUE / b || a < Long.MIN_VALUE / b)) ||
                ((b < -1l) && (a > Long.MIN_VALUE / b || a < Long.MAX_VALUE / b)) ||
                ((b == -1l) && (a == Long.MIN_VALUE))) {
            throw new RuntimeException("OVERFLOW_IN_MULTIPLICATION");
        }
        return a * b;
    }

    public static double nextAfter(double d, double direction) {

        if (Double.isNaN(d) || Double.isNaN(direction)) {
            return Double.NaN;
        } else if (d == direction) {
            return direction;
        } else if (Double.isInfinite(d)) {
            return (d < 0) ? -Double.MAX_VALUE : Double.MAX_VALUE;
        } else if (d == 0) {
            return (direction < 0) ? -Double.MIN_VALUE : Double.MIN_VALUE;
        }
        final long bits = Double.doubleToRawLongBits(d);
        final long sign = bits & 0x8000000000000000L;
        if ((direction < d) ^ (sign == 0L)) {
            return Double.longBitsToDouble(sign | ((bits & 0x7fffffffffffffffL) + 1));
        } else {
            return Double.longBitsToDouble(sign | ((bits & 0x7fffffffffffffffL) - 1));
        }

    }

    public static float nextAfter(final float f, final double direction) {

        if (Double.isNaN(f) || Double.isNaN(direction)) {
            return Float.NaN;
        } else if (f == direction) {
            return (float) direction;
        } else if (Float.isInfinite(f)) {
            return (f < 0f) ? -Float.MAX_VALUE : Float.MAX_VALUE;
        } else if (f == 0f) {
            return (direction < 0) ? -Float.MIN_VALUE : Float.MIN_VALUE;
        }

        final int bits = Float.floatToIntBits(f);
        final int sign = bits & 0x80000000;
        if ((direction < f) ^ (sign == 0)) {
            return Float.intBitsToFloat(sign | ((bits & 0x7fffffff) + 1));
        } else {
            return Float.intBitsToFloat(sign | ((bits & 0x7fffffff) - 1));
        }

    }

    public static double nextDown(final double a) {
        return nextAfter(a, Double.NEGATIVE_INFINITY);
    }

    public static float nextDown(final float a) {
        return nextAfter(a, Float.NEGATIVE_INFINITY);
    }

    public static double nextUp(final double a) {
        return nextAfter(a, Double.POSITIVE_INFINITY);
    }

    public static float nextUp(final float a) {
        return nextAfter(a, Float.POSITIVE_INFINITY);
    }

    private static double polyCosine(double x) {
        double x2 = x * x;

        double p = 2.479773539153719E-5;
        p = p * x2 + -0.0013888888689039883;
        p = p * x2 + 0.041666666666621166;
        p = p * x2 + -0.49999999999999994;
        p *= x2;

        return p;
    }

    private static double polySine(final double x) {
        double x2 = x * x;

        double p = 2.7553817452272217E-6;
        p = p * x2 + -1.9841269659586505E-4;
        p = p * x2 + 0.008333333333329196;
        p = p * x2 + -0.16666666666666666;
        p = p * x2 * x;

        return p;
    }

    public static double pow(double d, int e) {
        return pow(d, (long) e);
    }

    public static double pow(double d, long e) {
        if (e == 0) {
            return 1.0;
        } else if (e > 0) {
            return new Split(d).pow(e).full;
        } else {
            return new Split(d).reciprocal().pow(-e).full;
        }
    }

    private static void reducePayneHanek(double x, double[] result) {
        long inbits = Double.doubleToRawLongBits(x);
        int exponent = (int) ((inbits >> 52) & 0x7ff) - 1023;

        inbits &= 0x000fffffffffffffL;
        inbits |= 0x0010000000000000L;

        exponent++;
        inbits <<= 11;

        long shpi0;
        long shpiA;
        long shpiB;
        int idx = exponent >> 6;
        int shift = exponent - (idx << 6);

        if (shift != 0) {
            shpi0 = (idx == 0) ? 0 : (RECIP_2PI[idx - 1] << shift);
            shpi0 |= RECIP_2PI[idx] >>> (64 - shift);
            shpiA = (RECIP_2PI[idx] << shift) | (RECIP_2PI[idx + 1] >>> (64 - shift));
            shpiB = (RECIP_2PI[idx + 1] << shift) | (RECIP_2PI[idx + 2] >>> (64 - shift));
        } else {
            shpi0 = (idx == 0) ? 0 : RECIP_2PI[idx - 1];
            shpiA = RECIP_2PI[idx];
            shpiB = RECIP_2PI[idx + 1];
        }

        long a = inbits >>> 32;
        long b = inbits & 0xffffffffL;

        long c = shpiA >>> 32;
        long d = shpiA & 0xffffffffL;

        long ac = a * c;
        long bd = b * d;
        long bc = b * c;
        long ad = a * d;

        long prodB = bd + (ad << 32);
        long prodA = ac + (ad >>> 32);

        boolean bita = (bd & 0x8000000000000000L) != 0;
        boolean bitb = (ad & 0x80000000L) != 0;
        boolean bitsum = (prodB & 0x8000000000000000L) != 0;

        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prodA++;
        }

        bita = (prodB & 0x8000000000000000L) != 0;
        bitb = (bc & 0x80000000L) != 0;

        prodB += bc << 32;
        prodA += bc >>> 32;

        bitsum = (prodB & 0x8000000000000000L) != 0;

        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prodA++;
        }

        c = shpiB >>> 32;
        d = shpiB & 0xffffffffL;
        ac = a * c;
        bc = b * c;
        ad = a * d;

        ac += (bc + ad) >>> 32;

        bita = (prodB & 0x8000000000000000L) != 0;
        bitb = (ac & 0x8000000000000000L) != 0;
        prodB += ac;
        bitsum = (prodB & 0x8000000000000000L) != 0;
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prodA++;
        }

        c = shpi0 >>> 32;
        d = shpi0 & 0xffffffffL;

        bd = b * d;
        bc = b * c;
        ad = a * d;

        prodA += bd + ((bc + ad) << 32);


        int intPart = (int) (prodA >>> 62);

        prodA <<= 2;
        prodA |= prodB >>> 62;
        prodB <<= 2;

        a = prodA >>> 32;
        b = prodA & 0xffffffffL;

        c = PI_O_4_BITS[0] >>> 32;
        d = PI_O_4_BITS[0] & 0xffffffffL;

        ac = a * c;
        bd = b * d;
        bc = b * c;
        ad = a * d;

        long prod2B = bd + (ad << 32);
        long prod2A = ac + (ad >>> 32);

        bita = (bd & 0x8000000000000000L) != 0;
        bitb = (ad & 0x80000000L) != 0;
        bitsum = (prod2B & 0x8000000000000000L) != 0;

        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prod2A++;
        }

        bita = (prod2B & 0x8000000000000000L) != 0;
        bitb = (bc & 0x80000000L) != 0;

        prod2B += bc << 32;
        prod2A += bc >>> 32;

        bitsum = (prod2B & 0x8000000000000000L) != 0;

        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prod2A++;
        }

        c = PI_O_4_BITS[1] >>> 32;
        d = PI_O_4_BITS[1] & 0xffffffffL;
        ac = a * c;
        bc = b * c;
        ad = a * d;

        ac += (bc + ad) >>> 32;

        bita = (prod2B & 0x8000000000000000L) != 0;
        bitb = (ac & 0x8000000000000000L) != 0;
        prod2B += ac;
        bitsum = (prod2B & 0x8000000000000000L) != 0;
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prod2A++;
        }

        a = prodB >>> 32;
        b = prodB & 0xffffffffL;
        c = PI_O_4_BITS[0] >>> 32;
        d = PI_O_4_BITS[0] & 0xffffffffL;
        ac = a * c;
        bc = b * c;
        ad = a * d;

        ac += (bc + ad) >>> 32;

        bita = (prod2B & 0x8000000000000000L) != 0;
        bitb = (ac & 0x8000000000000000L) != 0;
        prod2B += ac;
        bitsum = (prod2B & 0x8000000000000000L) != 0;
        if ((bita && bitb) ||
                ((bita || bitb) && !bitsum)) {
            prod2A++;
        }

        double tmpA = (prod2A >>> 12) / TWO_POWER_52;
        double tmpB = (((prod2A & 0xfffL) << 40) + (prod2B >>> 24)) / TWO_POWER_52 / TWO_POWER_52;
        double sumA = tmpA + tmpB;
        double sumB = -(sumA - tmpA - tmpB);

        result[0] = intPart;
        result[1] = sumA * 2.0;
        result[2] = sumB * 2.0;
    }

    public static double rint(double x) {
        double y = floor(x);
        double d = x - y;

        if (d > 0.5) {
            if (y == -1.0) {
                return -0.0;
            }
            return y + 1.0;
        }
        if (d < 0.5) {
            return y;
        }

        long z = (long) y;
        return (z & 1) == 0 ? y : y + 1.0;
    }

    public static long round(double x) {
        return (long) floor(x + 0.5);
    }

    public static int round(final float x) {
        return (int) floor(x + 0.5f);
    }

    public static double scalb(final double d, final int n) {

        if ((n > -1023) && (n < 1024)) {
            return d * Double.longBitsToDouble(((long) (n + 1023)) << 52);
        }

        if (Double.isNaN(d) || Double.isInfinite(d) || (d == 0)) {
            return d;
        }
        if (n < -2098) {
            return (d > 0) ? 0.0 : -0.0;
        }
        if (n > 2097) {
            return (d > 0) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }

        final long bits = Double.doubleToRawLongBits(d);
        final long sign = bits & 0x8000000000000000L;
        int exponent = ((int) (bits >>> 52)) & 0x7ff;
        long mantissa = bits & 0x000fffffffffffffL;

        int scaledExponent = exponent + n;

        if (n < 0) {
            if (scaledExponent > 0) {
                return Double.longBitsToDouble(sign | (((long) scaledExponent) << 52) | mantissa);
            } else if (scaledExponent > -53) {

                mantissa |= 1L << 52;

                final long mostSignificantLostBit = mantissa & (1L << (-scaledExponent));
                mantissa >>>= 1 - scaledExponent;
                if (mostSignificantLostBit != 0) {
                    mantissa++;
                }
                return Double.longBitsToDouble(sign | mantissa);

            } else {
                return (sign == 0L) ? 0.0 : -0.0;
            }
        } else {
            if (exponent == 0) {

                while ((mantissa >>> 52) != 1) {
                    mantissa <<= 1;
                    --scaledExponent;
                }
                ++scaledExponent;
                mantissa &= 0x000fffffffffffffL;

                if (scaledExponent < 2047) {
                    return Double.longBitsToDouble(sign | (((long) scaledExponent) << 52) | mantissa);
                } else {
                    return (sign == 0L) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }

            } else if (scaledExponent < 2047) {
                return Double.longBitsToDouble(sign | (((long) scaledExponent) << 52) | mantissa);
            } else {
                return (sign == 0L) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            }
        }

    }

    public static float scalb(final float f, final int n) {

        if ((n > -127) && (n < 128)) {
            return f * Float.intBitsToFloat((n + 127) << 23);
        }

        if (Float.isNaN(f) || Float.isInfinite(f) || (f == 0f)) {
            return f;
        }
        if (n < -277) {
            return (f > 0) ? 0.0f : -0.0f;
        }
        if (n > 276) {
            return (f > 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }

        final int bits = Float.floatToIntBits(f);
        final int sign = bits & 0x80000000;
        int exponent = (bits >>> 23) & 0xff;
        int mantissa = bits & 0x007fffff;

        int scaledExponent = exponent + n;

        if (n < 0) {
            if (scaledExponent > 0) {
                return Float.intBitsToFloat(sign | (scaledExponent << 23) | mantissa);
            } else if (scaledExponent > -24) {

                mantissa |= 1 << 23;

                final int mostSignificantLostBit = mantissa & (1 << (-scaledExponent));
                mantissa >>>= 1 - scaledExponent;
                if (mostSignificantLostBit != 0) {
                    mantissa++;
                }
                return Float.intBitsToFloat(sign | mantissa);

            } else {
                return (sign == 0) ? 0.0f : -0.0f;
            }
        } else {
            if (exponent == 0) {

                while ((mantissa >>> 23) != 1) {
                    mantissa <<= 1;
                    --scaledExponent;
                }
                ++scaledExponent;
                mantissa &= 0x007fffff;

                if (scaledExponent < 255) {
                    return Float.intBitsToFloat(sign | (scaledExponent << 23) | mantissa);
                } else {
                    return (sign == 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
                }

            } else if (scaledExponent < 255) {
                return Float.intBitsToFloat(sign | (scaledExponent << 23) | mantissa);
            } else {
                return (sign == 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
            }
        }

    }

    public static double signum(final double a) {
        return (a < 0.0) ? -1.0 : ((a > 0.0) ? 1.0 : a);
    }

    public static float signum(final float a) {
        return (a < 0.0f) ? -1.0f : ((a > 0.0f) ? 1.0f : a);
    }

    public static double sin(double x) {
        boolean negative = false;
        int quadrant = 0;
        double xa;
        double xb = 0.0;

        xa = x;
        if (x < 0) {
            negative = true;
            xa = -xa;
        }

        if (xa == 0.0) {
            long bits = Double.doubleToRawLongBits(x);
            if (bits < 0) {
                return -0.0;
            }
            return 0.0;
        }

        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }

        if (xa > 3294198.0) {
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            final CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }

        if (negative) {
            quadrant ^= 2;
        }

        switch (quadrant) {
            case 0:
                return sinQ(xa, xb);
            case 1:
                return cosQ(xa, xb);
            case 2:
                return -sinQ(xa, xb);
            case 3:
                return -cosQ(xa, xb);
            default:
                return Double.NaN;
        }
    }

    private static double sinQ(double xa, double xb) {
        int idx = (int) ((xa * 8.0) + 0.5);
        final double epsilon = xa - EIGHTHS[idx];
        final double sintA = SINE_TABLE_A[idx];
        final double sintB = SINE_TABLE_B[idx];
        final double costA = COSINE_TABLE_A[idx];
        final double costB = COSINE_TABLE_B[idx];

        double sinEpsA = epsilon;
        double sinEpsB = polySine(epsilon);
        final double cosEpsA = 1.0;
        final double cosEpsB = polyCosine(epsilon);

        final double temp = sinEpsA * HEX_40000000;
        double temp2 = (sinEpsA + temp) - temp;
        sinEpsB += sinEpsA - temp2;
        sinEpsA = temp2;

        double result;


        double a = 0;
        double b = 0;

        double t = sintA;
        double c = a + t;
        double d = -(c - a - t);
        a = c;
        b += d;

        t = costA * sinEpsA;
        c = a + t;
        d = -(c - a - t);
        a = c;
        b += d;

        b = b + sintA * cosEpsB + costA * sinEpsB;

        b = b + sintB + costB * sinEpsA + sintB * cosEpsB + costB * sinEpsB;

        if (xb != 0.0) {
            t = ((costA + costB) * (cosEpsA + cosEpsB) -
                    (sintA + sintB) * (sinEpsA + sinEpsB)) * xb;
            c = a + t;
            d = -(c - a - t);
            a = c;
            b += d;
        }

        result = a + b;

        return result;
    }

    public static double sqrt(final double a) {
        return Math.sqrt(a);
    }

    public static int subtractExact(final int a, final int b) {

        final int sub = a - b;

        if ((a ^ b) < 0 && (sub ^ b) >= 0) {
            throw new RuntimeException("OVERFLOW_IN_SUBTRACTION");
        }

        return sub;

    }

    public static long subtractExact(final long a, final long b) {

        final long sub = a - b;

        if ((a ^ b) < 0 && (sub ^ b) >= 0) {
            throw new RuntimeException("OVERFLOW_IN_SUBTRACTION");
        }

        return sub;

    }

    public static double tan(double x) {
        boolean negative = false;
        int quadrant = 0;

        double xa = x;
        if (x < 0) {
            negative = true;
            xa = -xa;
        }

        if (xa == 0.0) {
            long bits = Double.doubleToRawLongBits(x);
            if (bits < 0) {
                return -0.0;
            }
            return 0.0;
        }

        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }

        double xb = 0;
        if (xa > 3294198.0) {
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            final CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }

        if (xa > 1.5) {
            final double pi2a = 1.5707963267948966;
            final double pi2b = 6.123233995736766E-17;

            final double a = pi2a - xa;
            double b = -(a - pi2a + xa);
            b += pi2b - xb;

            xa = a + b;
            xb = -(xa - a - b);
            quadrant ^= 1;
            negative ^= true;
        }

        double result;
        if ((quadrant & 1) == 0) {
            result = tanQ(xa, xb, false);
        } else {
            result = -tanQ(xa, xb, true);
        }

        if (negative) {
            result = -result;
        }

        return result;
    }

    private static double tanQ(double xa, double xb, boolean cotanFlag) {

        int idx = (int) ((xa * 8.0) + 0.5);
        final double epsilon = xa - EIGHTHS[idx];
        final double sintA = SINE_TABLE_A[idx];
        final double sintB = SINE_TABLE_B[idx];
        final double costA = COSINE_TABLE_A[idx];
        final double costB = COSINE_TABLE_B[idx];

        double sinEpsA = epsilon;
        double sinEpsB = polySine(epsilon);
        final double cosEpsA = 1.0;
        final double cosEpsB = polyCosine(epsilon);

        double temp = sinEpsA * HEX_40000000;
        double temp2 = (sinEpsA + temp) - temp;
        sinEpsB += sinEpsA - temp2;
        sinEpsA = temp2;


        double a = 0;
        double b = 0;

        double t = sintA;
        double c = a + t;
        double d = -(c - a - t);
        a = c;
        b += d;

        t = costA * sinEpsA;
        c = a + t;
        d = -(c - a - t);
        a = c;
        b += d;

        b += sintA * cosEpsB + costA * sinEpsB;
        b += sintB + costB * sinEpsA + sintB * cosEpsB + costB * sinEpsB;

        double sina = a + b;
        double sinb = -(sina - a - b);


        a = b = c = d = 0.0;

        t = costA * cosEpsA;
        c = a + t;
        d = -(c - a - t);
        a = c;
        b += d;

        t = -sintA * sinEpsA;
        c = a + t;
        d = -(c - a - t);
        a = c;
        b += d;

        b += costB * cosEpsA + costA * cosEpsB + costB * cosEpsB;
        b -= sintB * sinEpsA + sintA * sinEpsB + sintB * sinEpsB;

        double cosa = a + b;
        double cosb = -(cosa - a - b);

        if (cotanFlag) {
            double tmp;
            tmp = cosa;
            cosa = sina;
            sina = tmp;
            tmp = cosb;
            cosb = sinb;
            sinb = tmp;
        }


        double est = sina / cosa;

        temp = est * HEX_40000000;
        double esta = (est + temp) - temp;
        double estb = est - esta;

        temp = cosa * HEX_40000000;
        double cosaa = (cosa + temp) - temp;
        double cosab = cosa - cosaa;

        double err = (sina - esta * cosaa - esta * cosab - estb * cosaa - estb * cosab) / cosa;
        err += sinb / cosa;
        err += -sina * cosb / cosa / cosa;
        if (xb != 0.0) {
            double xbadj = xb + est * est * xb;
            if (cotanFlag) {
                xbadj = -xbadj;
            }

            err += xbadj;
        }

        return est + err;
    }

    public static double toDegrees(double x) {
        if (Double.isInfinite(x) || x == 0.0) {
            return x;
        }

        final double facta = 57.2957763671875;
        final double factb = 3.145894820876798E-6;

        double xa = doubleHighPart(x);
        double xb = x - xa;

        return xb * factb + xb * facta + xa * factb + xa * facta;
    }

    public static int toIntExact(final long n) throws RuntimeException {
        if (n < Integer.MIN_VALUE || n > Integer.MAX_VALUE) {
            throw new RuntimeException("OVERFLOW");
        }
        return (int) n;
    }

    public static double toRadians(double x) {
        if (Double.isInfinite(x) || x == 0.0) {
            return x;
        }

        final double facta = 0.01745329052209854;
        final double factb = 1.997844754509471E-9;

        double xa = doubleHighPart(x);
        double xb = x - xa;

        double result = xb * factb + xb * facta + xa * factb + xa * facta;
        if (result == 0) {
            result *= x;
        }
        return result;
    }

    public static double ulp(double x) {
        if (Double.isInfinite(x)) {
            return Double.POSITIVE_INFINITY;
        }
        return abs(x - Double.longBitsToDouble(Double.doubleToRawLongBits(x) ^ 1));
    }

    public static float ulp(float x) {
        if (Float.isInfinite(x)) {
            return Float.POSITIVE_INFINITY;
        }
        return abs(x - Float.intBitsToFloat(Float.floatToIntBits(x) ^ 1));
    }
}

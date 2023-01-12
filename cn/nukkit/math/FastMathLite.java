/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.NOBF;

@NOBF
public class FastMathLite {
    private static final double[] a = new double[]{0.0, 0.1246747374534607, 0.24740394949913025, 0.366272509098053, 0.4794255495071411, 0.5850973129272461, 0.6816387176513672, 0.7675435543060303, 0.8414709568023682, 0.902267575263977, 0.9489846229553223, 0.980893f, 0.9974949359893799, 0.9985313415527344};
    private static final double[] d = new double[]{0.0, -4.068233003401932E-9, 9.755392680573412E-9, 1.9987994582857286E-8, -1.0902938113007961E-8, -3.9986783938944604E-8, 4.23719669792332E-8, -5.207000323380292E-8, 2.800552834259E-8, 1.883511811213715E-8, -3.5997360512765566E-9, 4.116164446561962E-8, 5.0614674548127384E-8, -1.0129027912496858E-9};
    private static final double[] c = new double[]{1.0, 0.9921976327896118, 0.9689123630523682, 0.9305076599121094, 0.8775825500488281, 0.8109631538391113, 0.7316888570785522, 0.6409968137741089, 0.5403022766113281, 0.4311765432357788, 0.3153223395347595, 0.19454771280288696, 0.0707372f, -0.05417713522911072};
    private static final double[] n = new double[]{0.0, 3.4439717236742845E-8, 5.865827662008209E-8, -3.7999795083850525E-8, 1.184154459111628E-8, -3.43338934259355E-8, 1.1795268640216787E-8, 4.438921624363781E-8, 2.925681159240093E-8, -2.6437112632041807E-8, 2.2860509143963117E-8, -4.813899778443457E-9, 3.6725170580355583E-9, 2.0217439756338078E-10};
    private static final double[] o = new double[]{0.0, 0.1256551444530487, 0.25534194707870483, 0.3936265707015991, 0.5463024377822876, 0.7214844226837158, 0.9315965175628662, 1.1974215507507324, 1.5574076175689697, 2.092571258544922, 3.0095696449279785, 5.041914939880371, 14.101419448852539, -18.430862426757812};
    private static final double[] h = new double[]{0.0, -7.877917738262007E-9, -2.5857668567479893E-8, 5.2240336371356666E-9, 5.206150291559893E-8, 1.8307188599677033E-8, -5.7618793749770706E-8, 7.848361555046424E-8, 1.0708593250394448E-7, 1.7827257129423813E-8, 2.893485277253286E-8, 3.1660099222737955E-7, 4.983191803254889E-7, -3.356118100840571E-7};
    private static final long[] g = new long[]{2935890503282001226L, 9154082963658192752L, 3952090531849364496L, 9193070505571053912L, 7910884519577875640L, 113236205062349959L, 4577762542105553359L, -5034868814120038111L, 4208363204685324176L, 5648769086999809661L, 2819561105158720014L, -4035746434778044925L, -302932621132653753L, -2644281811660520851L, -3183605296591799669L, 6722166367014452318L, -3512299194304650054L, -7278142539171889152L};
    private static final long[] r = new long[]{-3958705157555305932L, -4267615245585081135L};
    private static final double[] m = new double[]{0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0, 1.125, 1.25, 1.375, 1.5, 1.625};
    private static final double[] i = new double[]{0.6299605249474366, 0.7937005259840998, 1.0, 1.2599210498948732, 1.5874010519681994};
    private static final long f = 0x40000000L;
    private static final long k = -1073741824L;
    private static final int e = Integer.MAX_VALUE;
    private static final long p = Long.MAX_VALUE;
    private static final double l = 4.503599627370496E15;
    private static final double j = 0.75;
    private static final double q = 0.5;
    private static final double t = 0.25;
    private static final double b = Double.longBitsToDouble(0x10000000000000L);
    private static final long s = 1023L;

    private FastMathLite() {
    }

    private static double b(double d2) {
        if (d2 > -b && d2 < b) {
            return d2;
        }
        long l = Double.doubleToRawLongBits(d2);
        return Double.longBitsToDouble(l &= 0xFFFFFFFFC0000000L);
    }

    public static double sqrt(double d2) {
        return Math.sqrt(d2);
    }

    public static double signum(double d2) {
        return d2 < 0.0 ? -1.0 : (d2 > 0.0 ? 1.0 : d2);
    }

    public static float signum(float f2) {
        return f2 < 0.0f ? -1.0f : (f2 > 0.0f ? 1.0f : f2);
    }

    public static double nextUp(double d2) {
        return FastMathLite.nextAfter(d2, Double.POSITIVE_INFINITY);
    }

    public static float nextUp(float f2) {
        return FastMathLite.nextAfter(f2, Double.POSITIVE_INFINITY);
    }

    public static double nextDown(double d2) {
        return FastMathLite.nextAfter(d2, Double.NEGATIVE_INFINITY);
    }

    public static float nextDown(float f2) {
        return FastMathLite.nextAfter(f2, Double.NEGATIVE_INFINITY);
    }

    public static double random() {
        return Math.random();
    }

    public static double pow(double d2, int n) {
        return FastMathLite.pow(d2, (long)n);
    }

    public static double pow(double d2, long l) {
        if (l == 0L) {
            return 1.0;
        }
        if (l > 0L) {
            return new Split(d2).a(l).b;
        }
        return new Split(d2).reciprocal().a(-l).b;
    }

    private static double c(double d2) {
        double d3 = d2 * d2;
        double d4 = 2.7553817452272217E-6;
        d4 = d4 * d3 + -1.9841269659586505E-4;
        d4 = d4 * d3 + 0.008333333333329196;
        d4 = d4 * d3 + -0.16666666666666666;
        d4 = d4 * d3 * d2;
        return d4;
    }

    private static double a(double d2) {
        double d3 = d2 * d2;
        double d4 = 2.479773539153719E-5;
        d4 = d4 * d3 + -0.0013888888689039883;
        d4 = d4 * d3 + 0.041666666666621166;
        d4 = d4 * d3 + -0.49999999999999994;
        return d4 *= d3;
    }

    private static double b(double d2, double d3) {
        int n = (int)(d2 * 8.0 + 0.5);
        double d4 = d2 - m[n];
        double d5 = a[n];
        double d6 = d[n];
        double d7 = c[n];
        double d8 = FastMathLite.n[n];
        double d9 = d4;
        double d10 = FastMathLite.c(d4);
        double d11 = 1.0;
        double d12 = FastMathLite.a(d4);
        double d13 = d9 * 1.073741824E9;
        double d14 = d9 + d13 - d13;
        d10 += d9 - d14;
        d9 = d14;
        double d15 = 0.0;
        double d16 = 0.0;
        double d17 = d5;
        double d18 = d15 + d17;
        double d19 = -(d18 - d15 - d17);
        d15 = d18;
        d16 += d19;
        d17 = d7 * d9;
        d18 = d15 + d17;
        d19 = -(d18 - d15 - d17);
        d15 = d18;
        d16 += d19;
        d16 = d16 + d5 * d12 + d7 * d10;
        d16 = d16 + d6 + d8 * d9 + d6 * d12 + d8 * d10;
        if (d3 != 0.0) {
            d17 = ((d7 + d8) * (1.0 + d12) - (d5 + d6) * (d9 + d10)) * d3;
            d18 = d15 + d17;
            d19 = -(d18 - d15 - d17);
            d15 = d18;
            d16 += d19;
        }
        double d20 = d15 + d16;
        return d20;
    }

    private static double a(double d2, double d3) {
        double d4 = 1.5707963267948966;
        double d5 = 6.123233995736766E-17;
        double d6 = 1.5707963267948966 - d2;
        double d7 = -(d6 - 1.5707963267948966 + d2);
        return FastMathLite.b(d6, d7 += 6.123233995736766E-17 - d3);
    }

    private static double b(double d2, double d3, boolean bl) {
        double d4;
        int n = (int)(d2 * 8.0 + 0.5);
        double d5 = d2 - m[n];
        double d6 = a[n];
        double d7 = d[n];
        double d8 = c[n];
        double d9 = FastMathLite.n[n];
        double d10 = d5;
        double d11 = FastMathLite.c(d5);
        double d12 = 1.0;
        double d13 = FastMathLite.a(d5);
        double d14 = d10 * 1.073741824E9;
        double d15 = d10 + d14 - d14;
        d11 += d10 - d15;
        d10 = d15;
        double d16 = 0.0;
        double d17 = 0.0;
        double d18 = d6;
        double d19 = d16 + d18;
        double d20 = -(d19 - d16 - d18);
        d16 = d19;
        d17 += d20;
        d18 = d8 * d10;
        d19 = d16 + d18;
        d20 = -(d19 - d16 - d18);
        d16 = d19;
        d17 += d20;
        d17 += d6 * d13 + d8 * d11;
        double d21 = d16 + (d17 += d7 + d9 * d10 + d7 * d13 + d9 * d11);
        double d22 = -(d21 - d16 - d17);
        d20 = 0.0;
        d19 = 0.0;
        d17 = 0.0;
        d16 = 0.0;
        d18 = d8 * 1.0;
        d19 = d16 + d18;
        d20 = -(d19 - d16 - d18);
        d16 = d19;
        d17 += d20;
        d18 = -d6 * d10;
        d19 = d16 + d18;
        d20 = -(d19 - d16 - d18);
        d16 = d19;
        d17 += d20;
        d17 += d9 * 1.0 + d8 * d13 + d9 * d13;
        double d23 = d16 + (d17 -= d7 * d10 + d6 * d11 + d7 * d11);
        double d24 = -(d23 - d16 - d17);
        if (bl) {
            d4 = d23;
            d23 = d21;
            d21 = d4;
            d4 = d24;
            d24 = d22;
            d22 = d4;
        }
        d4 = d21 / d23;
        d14 = d4 * 1.073741824E9;
        double d25 = d4 + d14 - d14;
        double d26 = d4 - d25;
        d14 = d23 * 1.073741824E9;
        double d27 = d23 + d14 - d14;
        double d28 = d23 - d27;
        double d29 = (d21 - d25 * d27 - d25 * d28 - d26 * d27 - d26 * d28) / d23;
        d29 += d22 / d23;
        d29 += -d21 * d24 / d23 / d23;
        if (d3 != 0.0) {
            double d30 = d3 + d4 * d4 * d3;
            if (bl) {
                d30 = -d30;
            }
            d29 += d30;
        }
        return d4 + d29;
    }

    private static void a(double d2, double[] dArray) {
        boolean bl;
        long l;
        long l2;
        long l3;
        int n;
        int n2;
        long l4 = Double.doubleToRawLongBits(d2);
        int n3 = (int)(l4 >> 52 & 0x7FFL) - 1023;
        l4 &= 0xFFFFFFFFFFFFFL;
        l4 |= 0x10000000000000L;
        l4 <<= 11;
        if ((n2 = ++n3 - ((n = n3 >> 6) << 6)) != 0) {
            l3 = n == 0 ? 0L : g[n - 1] << n2;
            l3 |= g[n] >>> 64 - n2;
            l2 = g[n] << n2 | g[n + 1] >>> 64 - n2;
            l = g[n + 1] << n2 | g[n + 2] >>> 64 - n2;
        } else {
            l3 = n == 0 ? 0L : g[n - 1];
            l2 = g[n];
            l = g[n + 1];
        }
        long l5 = l4 >>> 32;
        long l6 = l4 & 0xFFFFFFFFL;
        long l7 = l2 >>> 32;
        long l8 = l2 & 0xFFFFFFFFL;
        long l9 = l5 * l7;
        long l10 = l6 * l8;
        long l11 = l6 * l7;
        long l12 = l5 * l8;
        long l13 = l10 + (l12 << 32);
        long l14 = l9 + (l12 >>> 32);
        boolean bl2 = (l10 & Long.MIN_VALUE) != 0L;
        boolean bl3 = (l12 & 0x80000000L) != 0L;
        boolean bl4 = bl = (l13 & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l14;
        }
        bl2 = (l13 & Long.MIN_VALUE) != 0L;
        bl3 = (l11 & 0x80000000L) != 0L;
        l14 += l11 >>> 32;
        boolean bl5 = bl = ((l13 += l11 << 32) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l14;
        }
        l7 = l >>> 32;
        l8 = l & 0xFFFFFFFFL;
        l9 = l5 * l7;
        l11 = l6 * l7;
        l12 = l5 * l8;
        bl2 = (l13 & Long.MIN_VALUE) != 0L;
        bl3 = ((l9 += l11 + l12 >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl6 = bl = ((l13 += l9) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l14;
        }
        l7 = l3 >>> 32;
        l8 = l3 & 0xFFFFFFFFL;
        l10 = l6 * l8;
        l11 = l6 * l7;
        l12 = l5 * l8;
        int n4 = (int)((l14 += l10 + (l11 + l12 << 32)) >>> 62);
        l14 <<= 2;
        l14 |= l13 >>> 62;
        l13 <<= 2;
        l5 = l14 >>> 32;
        l6 = l14 & 0xFFFFFFFFL;
        l7 = r[0] >>> 32;
        l8 = r[0] & 0xFFFFFFFFL;
        l9 = l5 * l7;
        l10 = l6 * l8;
        l11 = l6 * l7;
        l12 = l5 * l8;
        long l15 = l10 + (l12 << 32);
        long l16 = l9 + (l12 >>> 32);
        bl2 = (l10 & Long.MIN_VALUE) != 0L;
        bl3 = (l12 & 0x80000000L) != 0L;
        boolean bl7 = bl = (l15 & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l16;
        }
        bl2 = (l15 & Long.MIN_VALUE) != 0L;
        bl3 = (l11 & 0x80000000L) != 0L;
        l16 += l11 >>> 32;
        boolean bl8 = bl = ((l15 += l11 << 32) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l16;
        }
        l7 = r[1] >>> 32;
        l8 = r[1] & 0xFFFFFFFFL;
        l9 = l5 * l7;
        l11 = l6 * l7;
        l12 = l5 * l8;
        bl2 = (l15 & Long.MIN_VALUE) != 0L;
        bl3 = ((l9 += l11 + l12 >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl9 = bl = ((l15 += l9) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l16;
        }
        l5 = l13 >>> 32;
        l6 = l13 & 0xFFFFFFFFL;
        l7 = r[0] >>> 32;
        l8 = r[0] & 0xFFFFFFFFL;
        l9 = l5 * l7;
        l11 = l6 * l7;
        l12 = l5 * l8;
        bl2 = (l15 & Long.MIN_VALUE) != 0L;
        bl3 = ((l9 += l11 + l12 >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl10 = bl = ((l15 += l9) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l16;
        }
        double d3 = (double)(l16 >>> 12) / 4.503599627370496E15;
        double d4 = (double)(((l16 & 0xFFFL) << 40) + (l15 >>> 24)) / 4.503599627370496E15 / 4.503599627370496E15;
        double d5 = d3 + d4;
        double d6 = -(d5 - d3 - d4);
        dArray[0] = n4;
        dArray[1] = d5 * 2.0;
        dArray[2] = d6 * 2.0;
    }

    public static double sin(double d2) {
        boolean bl = false;
        int n = 0;
        double d3 = 0.0;
        double d4 = d2;
        if (d2 < 0.0) {
            bl = true;
            d4 = -d4;
        }
        if (d4 == 0.0) {
            long l = Double.doubleToRawLongBits(d2);
            if (l < 0L) {
                return -0.0;
            }
            return 0.0;
        }
        if (d4 != d4 || d4 == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        if (d4 > 3294198.0) {
            double[] dArray = new double[3];
            FastMathLite.a(d4, dArray);
            n = (int)dArray[0] & 3;
            d4 = dArray[1];
            d3 = dArray[2];
        } else if (d4 > 1.5707963267948966) {
            CodyWaite codyWaite = new CodyWaite(d4);
            n = codyWaite.b() & 3;
            d4 = codyWaite.c();
            d3 = codyWaite.a();
        }
        if (bl) {
            n ^= 2;
        }
        switch (n) {
            case 0: {
                return FastMathLite.b(d4, d3);
            }
            case 1: {
                return FastMathLite.a(d4, d3);
            }
            case 2: {
                return -FastMathLite.b(d4, d3);
            }
            case 3: {
                return -FastMathLite.a(d4, d3);
            }
        }
        return Double.NaN;
    }

    public static double cos(double d2) {
        int n = 0;
        double d3 = d2;
        if (d2 < 0.0) {
            d3 = -d3;
        }
        if (d3 != d3 || d3 == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double d4 = 0.0;
        if (d3 > 3294198.0) {
            double[] dArray = new double[3];
            FastMathLite.a(d3, dArray);
            n = (int)dArray[0] & 3;
            d3 = dArray[1];
            d4 = dArray[2];
        } else if (d3 > 1.5707963267948966) {
            CodyWaite codyWaite = new CodyWaite(d3);
            n = codyWaite.b() & 3;
            d3 = codyWaite.c();
            d4 = codyWaite.a();
        }
        switch (n) {
            case 0: {
                return FastMathLite.a(d3, d4);
            }
            case 1: {
                return -FastMathLite.b(d3, d4);
            }
            case 2: {
                return -FastMathLite.a(d3, d4);
            }
            case 3: {
                return FastMathLite.b(d3, d4);
            }
        }
        return Double.NaN;
    }

    public static double tan(double d2) {
        Object object;
        boolean bl = false;
        int n = 0;
        double d3 = d2;
        if (d2 < 0.0) {
            bl = true;
            d3 = -d3;
        }
        if (d3 == 0.0) {
            long l = Double.doubleToRawLongBits(d2);
            if (l < 0L) {
                return -0.0;
            }
            return 0.0;
        }
        if (d3 != d3 || d3 == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double d4 = 0.0;
        if (d3 > 3294198.0) {
            object = new double[3];
            FastMathLite.a(d3, (double[])object);
            n = (int)object[0] & 3;
            d3 = (double)object[1];
            d4 = (double)object[2];
        } else if (d3 > 1.5707963267948966) {
            object = new CodyWaite(d3);
            n = ((CodyWaite)object).b() & 3;
            d3 = ((CodyWaite)object).c();
            d4 = ((CodyWaite)object).a();
        }
        if (d3 > 1.5) {
            double d5 = 1.5707963267948966;
            double d6 = 6.123233995736766E-17;
            double d7 = 1.5707963267948966 - d3;
            double d8 = -(d7 - 1.5707963267948966 + d3);
            d3 = d7 + (d8 += 6.123233995736766E-17 - d4);
            d4 = -(d3 - d7 - d8);
            n ^= 1;
            bl ^= true;
        }
        double d9 = !(n & true) ? FastMathLite.b(d3, d4, false) : -FastMathLite.b(d3, d4, true);
        if (bl) {
            d9 = -d9;
        }
        return d9;
    }

    public static double atan(double d2) {
        return FastMathLite.a(d2, 0.0, false);
    }

    private static double a(double d2, double d3, boolean bl) {
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;
        int n;
        boolean bl2;
        if (d2 == 0.0) {
            return bl ? FastMathLite.copySign(Math.PI, d2) : d2;
        }
        if (d2 < 0.0) {
            d2 = -d2;
            d3 = -d3;
            bl2 = true;
        } else {
            bl2 = false;
        }
        if (d2 > 1.633123935319537E16) {
            return bl2 ^ bl ? -1.5707963267948966 : 1.5707963267948966;
        }
        if (d2 < 1.0) {
            n = (int)((-1.7168146928204135 * d2 * d2 + 8.0) * d2 + 0.5);
        } else {
            d11 = 1.0 / d2;
            n = (int)(-((-1.7168146928204135 * d11 * d11 + 8.0) * d11) + 13.07);
        }
        d11 = o[n];
        double d12 = h[n];
        double d13 = d2 - d11;
        double d14 = -(d13 - d2 + d11);
        double d15 = d13 + (d14 += d3 - d12);
        d14 = -(d15 - d13 - d14);
        d13 = d15;
        d15 = d2 * 1.073741824E9;
        double d16 = d2 + d15 - d15;
        double d17 = d3 + d2 - d16;
        d2 = d16;
        d3 += d17;
        if (n == 0) {
            d10 = 1.0 / (1.0 + (d2 + d3) * (d11 + d12));
            d16 = d13 * d10;
            d17 = d14 * d10;
        } else {
            d10 = d2 * d11;
            d9 = 1.0 + d10;
            d8 = -(d9 - 1.0 - d10);
            d10 = d3 * d11 + d2 * d12;
            d15 = d9 + d10;
            d8 += -(d15 - d9 - d10);
            d9 = d15;
            d8 += d3 * d12;
            d16 = d13 / d9;
            d15 = d16 * 1.073741824E9;
            d7 = d16 + d15 - d15;
            d6 = d16 - d7;
            d15 = d9 * 1.073741824E9;
            d5 = d9 + d15 - d15;
            d4 = d9 - d5;
            d17 = (d13 - d7 * d5 - d7 * d4 - d6 * d5 - d6 * d4) / d9;
            d17 += -d13 * d8 / d9 / d9;
            d17 += d14 / d9;
        }
        d13 = d16;
        d14 = d17;
        d10 = d13 * d13;
        d17 = 0.07490822288864472;
        d17 = d17 * d10 - 0.09088450866185192;
        d17 = d17 * d10 + 0.11111095942313305;
        d17 = d17 * d10 - 0.1428571423679182;
        d17 = d17 * d10 + 0.19999999999923582;
        d17 = d17 * d10 - 0.33333333333333287;
        d17 = d17 * d10 * d13;
        d16 = d13;
        d15 = d16 + d17;
        d17 = -(d15 - d16 - d17);
        d16 = d15;
        d9 = m[n];
        d8 = d9 + d16;
        d7 = -(d8 - d9 - d16);
        d15 = d8 + (d17 += d14 / (1.0 + d13 * d13));
        d7 += -(d15 - d8 - d17);
        d8 = d15;
        d6 = d8 + d7;
        if (bl) {
            d5 = -(d6 - d8 - d7);
            d4 = Math.PI;
            double d18 = 1.2246467991473532E-16;
            d8 = Math.PI - d6;
            d7 = -(d8 - Math.PI + d6);
            d6 = d8 + (d7 += 1.2246467991473532E-16 - d5);
        }
        if (bl2 ^ bl) {
            d6 = -d6;
        }
        return d6;
    }

    public static double atan2(double d2, double d3) {
        double d4;
        if (d3 != d3 || d2 != d2) {
            return Double.NaN;
        }
        if (d2 == 0.0) {
            double d5 = d3 * d2;
            double d6 = 1.0 / d3;
            double d7 = 1.0 / d2;
            if (d6 == 0.0) {
                if (d3 > 0.0) {
                    return d2;
                }
                return FastMathLite.copySign(Math.PI, d2);
            }
            if (d3 < 0.0 || d6 < 0.0) {
                if (d2 < 0.0 || d7 < 0.0) {
                    return -Math.PI;
                }
                return Math.PI;
            }
            return d5;
        }
        if (d2 == Double.POSITIVE_INFINITY) {
            if (d3 == Double.POSITIVE_INFINITY) {
                return 0.7853981633974483;
            }
            if (d3 == Double.NEGATIVE_INFINITY) {
                return 2.356194490192345;
            }
            return 1.5707963267948966;
        }
        if (d2 == Double.NEGATIVE_INFINITY) {
            if (d3 == Double.POSITIVE_INFINITY) {
                return -0.7853981633974483;
            }
            if (d3 == Double.NEGATIVE_INFINITY) {
                return -2.356194490192345;
            }
            return -1.5707963267948966;
        }
        if (d3 == Double.POSITIVE_INFINITY) {
            if (d2 > 0.0 || 1.0 / d2 > 0.0) {
                return 0.0;
            }
            if (d2 < 0.0 || 1.0 / d2 < 0.0) {
                return -0.0;
            }
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            if (d2 > 0.0 || 1.0 / d2 > 0.0) {
                return Math.PI;
            }
            if (d2 < 0.0 || 1.0 / d2 < 0.0) {
                return -Math.PI;
            }
        }
        if (d3 == 0.0) {
            if (d2 > 0.0 || 1.0 / d2 > 0.0) {
                return 1.5707963267948966;
            }
            if (d2 < 0.0 || 1.0 / d2 < 0.0) {
                return -1.5707963267948966;
            }
        }
        if (Double.isInfinite(d4 = d2 / d3)) {
            return FastMathLite.a(d4, 0.0, d3 < 0.0);
        }
        double d8 = FastMathLite.b(d4);
        double d9 = d4 - d8;
        double d10 = FastMathLite.b(d3);
        double d11 = d3 - d10;
        d9 += (d2 - d8 * d10 - d8 * d11 - d9 * d10 - d9 * d11) / d3;
        double d12 = d8 + d9;
        d9 = -(d12 - d8 - d9);
        d8 = d12;
        if (d8 == 0.0) {
            d8 = FastMathLite.copySign(0.0, d2);
        }
        return FastMathLite.a(d8, d9, d3 < 0.0);
    }

    public static double asin(double d2) {
        if (d2 != d2) {
            return Double.NaN;
        }
        if (d2 > 1.0 || d2 < -1.0) {
            return Double.NaN;
        }
        if (d2 == 1.0) {
            return 1.5707963267948966;
        }
        if (d2 == -1.0) {
            return -1.5707963267948966;
        }
        if (d2 == 0.0) {
            return d2;
        }
        double d3 = d2 * 1.073741824E9;
        double d4 = d2 + d3 - d3;
        double d5 = d2 - d4;
        double d6 = d4 * d4;
        double d7 = d4 * d5 * 2.0 + d5 * d5;
        d6 = -d6;
        d7 = -d7;
        double d8 = 1.0 + d6;
        double d9 = -(d8 - 1.0 - d6);
        d3 = d8 + d7;
        d9 += -(d3 - d8 - d7);
        d8 = d3;
        double d10 = FastMathLite.sqrt(d8);
        d3 = d10 * 1.073741824E9;
        d6 = d10 + d3 - d3;
        d7 = d10 - d6;
        d7 += (d8 - d6 * d6 - 2.0 * d6 * d7 - d7 * d7) / (2.0 * d10);
        double d11 = d9 / (2.0 * d10);
        double d12 = d2 / d10;
        d3 = d12 * 1.073741824E9;
        double d13 = d12 + d3 - d3;
        double d14 = d12 - d13;
        d14 += (d2 - d13 * d6 - d13 * d7 - d14 * d6 - d14 * d7) / d10;
        d3 = d13 + (d14 += -d2 * d11 / d10 / d10);
        d14 = -(d3 - d13 - d14);
        d13 = d3;
        return FastMathLite.a(d13, d14, false);
    }

    public static double acos(double d2) {
        if (d2 != d2) {
            return Double.NaN;
        }
        if (d2 > 1.0 || d2 < -1.0) {
            return Double.NaN;
        }
        if (d2 == -1.0) {
            return Math.PI;
        }
        if (d2 == 1.0) {
            return 0.0;
        }
        if (d2 == 0.0) {
            return 1.5707963267948966;
        }
        double d3 = d2 * 1.073741824E9;
        double d4 = d2 + d3 - d3;
        double d5 = d2 - d4;
        double d6 = d4 * d4;
        double d7 = d4 * d5 * 2.0 + d5 * d5;
        d6 = -d6;
        d7 = -d7;
        double d8 = 1.0 + d6;
        double d9 = -(d8 - 1.0 - d6);
        d3 = d8 + d7;
        d9 += -(d3 - d8 - d7);
        d8 = d3;
        double d10 = FastMathLite.sqrt(d8);
        d3 = d10 * 1.073741824E9;
        d6 = d10 + d3 - d3;
        d7 = d10 - d6;
        d7 += (d8 - d6 * d6 - 2.0 * d6 * d7 - d7 * d7) / (2.0 * d10);
        d7 += d9 / (2.0 * d10);
        d10 = d6 + d7;
        d7 = -(d10 - d6 - d7);
        double d11 = d10 / d2;
        if (Double.isInfinite(d11)) {
            return 1.5707963267948966;
        }
        double d12 = FastMathLite.b(d11);
        double d13 = d11 - d12;
        d13 += (d10 - d12 * d4 - d12 * d5 - d13 * d4 - d13 * d5) / d2;
        d3 = d12 + (d13 += d7 / d2);
        d13 = -(d3 - d12 - d13);
        d12 = d3;
        return FastMathLite.a(d12, d13, d2 < 0.0);
    }

    public static double cbrt(double d2) {
        long l = Double.doubleToRawLongBits(d2);
        int n = (int)(l >> 52 & 0x7FFL) - 1023;
        boolean bl = false;
        if (n == -1023) {
            if (d2 == 0.0) {
                return d2;
            }
            bl = true;
            l = Double.doubleToRawLongBits(d2 *= 1.8014398509481984E16);
            n = (int)(l >> 52 & 0x7FFL) - 1023;
        }
        if (n == 1024) {
            return d2;
        }
        int n2 = n / 3;
        double d3 = Double.longBitsToDouble(l & Long.MIN_VALUE | (long)(n2 + 1023 & 0x7FF) << 52);
        double d4 = Double.longBitsToDouble(l & 0xFFFFFFFFFFFFFL | 0x3FF0000000000000L);
        double d5 = -0.010714690733195933;
        d5 = d5 * d4 + 0.0875862700108075;
        d5 = d5 * d4 + -0.3058015757857271;
        d5 = d5 * d4 + 0.7249995199969751;
        d5 = d5 * d4 + 0.5039018405998233;
        d5 *= i[n % 3 + 2];
        double d6 = d2 / (d3 * d3 * d3);
        d5 += (d6 - d5 * d5 * d5) / (3.0 * d5 * d5);
        d5 += (d6 - d5 * d5 * d5) / (3.0 * d5 * d5);
        double d7 = d5 * 1.073741824E9;
        double d8 = d5 + d7 - d7;
        double d9 = d5 - d8;
        double d10 = d8 * d8;
        double d11 = d8 * d9 * 2.0 + d9 * d9;
        d7 = d10 * 1.073741824E9;
        double d12 = d10 + d7 - d7;
        d11 += d10 - d12;
        d10 = d12;
        d11 = d10 * d9 + d8 * d11 + d11 * d9;
        double d13 = d6 - (d10 *= d8);
        double d14 = -(d13 - d6 + d10);
        d5 += (d13 + (d14 -= d11)) / (3.0 * d5 * d5);
        d5 *= d3;
        if (bl) {
            d5 *= 3.814697265625E-6;
        }
        return d5;
    }

    public static double toRadians(double d2) {
        if (Double.isInfinite(d2) || d2 == 0.0) {
            return d2;
        }
        double d3 = 0.01745329052209854;
        double d4 = 1.997844754509471E-9;
        double d5 = FastMathLite.b(d2);
        double d6 = d2 - d5;
        double d7 = d6 * 1.997844754509471E-9 + d6 * 0.01745329052209854 + d5 * 1.997844754509471E-9 + d5 * 0.01745329052209854;
        if (d7 == 0.0) {
            d7 *= d2;
        }
        return d7;
    }

    public static double toDegrees(double d2) {
        if (Double.isInfinite(d2) || d2 == 0.0) {
            return d2;
        }
        double d3 = 57.2957763671875;
        double d4 = 3.145894820876798E-6;
        double d5 = FastMathLite.b(d2);
        double d6 = d2 - d5;
        return d6 * 3.145894820876798E-6 + d6 * 57.2957763671875 + d5 * 3.145894820876798E-6 + d5 * 57.2957763671875;
    }

    public static int abs(int n) {
        int n2 = n >>> 31;
        return (n ^ ~n2 + 1) + n2;
    }

    public static long abs(long l) {
        long l2 = l >>> 63;
        return (l ^ (l2 ^ 0xFFFFFFFFFFFFFFFFL) + 1L) + l2;
    }

    public static float abs(float f2) {
        return Float.intBitsToFloat(Integer.MAX_VALUE & Float.floatToRawIntBits(f2));
    }

    public static double abs(double d2) {
        return Double.longBitsToDouble(Long.MAX_VALUE & Double.doubleToRawLongBits(d2));
    }

    public static double ulp(double d2) {
        if (Double.isInfinite(d2)) {
            return Double.POSITIVE_INFINITY;
        }
        return FastMathLite.abs(d2 - Double.longBitsToDouble(Double.doubleToRawLongBits(d2) ^ 1L));
    }

    public static float ulp(float f2) {
        if (Float.isInfinite(f2)) {
            return Float.POSITIVE_INFINITY;
        }
        return FastMathLite.abs(f2 - Float.intBitsToFloat(Float.floatToIntBits(f2) ^ 1));
    }

    public static double scalb(double d2, int n) {
        if (n > -1023 && n < 1024) {
            return d2 * Double.longBitsToDouble((long)(n + 1023) << 52);
        }
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 == 0.0) {
            return d2;
        }
        if (n < -2098) {
            return d2 > 0.0 ? 0.0 : -0.0;
        }
        if (n > 2097) {
            return d2 > 0.0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        long l = Double.doubleToRawLongBits(d2);
        long l2 = l & Long.MIN_VALUE;
        int n2 = (int)(l >>> 52) & 0x7FF;
        long l3 = l & 0xFFFFFFFFFFFFFL;
        int n3 = n2 + n;
        if (n < 0) {
            if (n3 > 0) {
                return Double.longBitsToDouble(l2 | (long)n3 << 52 | l3);
            }
            if (n3 > -53) {
                long l4 = (l3 |= 0x10000000000000L) & 1L << -n3;
                l3 >>>= 1 - n3;
                if (l4 != 0L) {
                    ++l3;
                }
                return Double.longBitsToDouble(l2 | l3);
            }
            return l2 == 0L ? 0.0 : -0.0;
        }
        if (n2 == 0) {
            while (l3 >>> 52 != 1L) {
                l3 <<= 1;
                --n3;
            }
            l3 &= 0xFFFFFFFFFFFFFL;
            if (++n3 < 2047) {
                return Double.longBitsToDouble(l2 | (long)n3 << 52 | l3);
            }
            return l2 == 0L ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        if (n3 < 2047) {
            return Double.longBitsToDouble(l2 | (long)n3 << 52 | l3);
        }
        return l2 == 0L ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }

    public static float scalb(float f2, int n) {
        if (n > -127 && n < 128) {
            return f2 * Float.intBitsToFloat(n + 127 << 23);
        }
        if (Float.isNaN(f2) || Float.isInfinite(f2) || f2 == 0.0f) {
            return f2;
        }
        if (n < -277) {
            return f2 > 0.0f ? 0.0f : -0.0f;
        }
        if (n > 276) {
            return f2 > 0.0f ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        int n2 = Float.floatToIntBits(f2);
        int n3 = n2 & Integer.MIN_VALUE;
        int n4 = n2 >>> 23 & 0xFF;
        int n5 = n2 & 0x7FFFFF;
        int n6 = n4 + n;
        if (n < 0) {
            if (n6 > 0) {
                return Float.intBitsToFloat(n3 | n6 << 23 | n5);
            }
            if (n6 > -24) {
                int n7 = (n5 |= 0x800000) & 1 << -n6;
                n5 >>>= 1 - n6;
                if (n7 != 0) {
                    ++n5;
                }
                return Float.intBitsToFloat(n3 | n5);
            }
            return n3 == 0 ? 0.0f : -0.0f;
        }
        if (n4 == 0) {
            while (n5 >>> 23 != 1) {
                n5 <<= 1;
                --n6;
            }
            n5 &= 0x7FFFFF;
            if (++n6 < 255) {
                return Float.intBitsToFloat(n3 | n6 << 23 | n5);
            }
            return n3 == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        if (n6 < 255) {
            return Float.intBitsToFloat(n3 | n6 << 23 | n5);
        }
        return n3 == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
    }

    public static double nextAfter(double d2, double d3) {
        long l;
        long l2;
        if (Double.isNaN(d2) || Double.isNaN(d3)) {
            return Double.NaN;
        }
        if (d2 == d3) {
            return d3;
        }
        if (Double.isInfinite(d2)) {
            return d2 < 0.0 ? -1.7976931348623157E308 : Double.MAX_VALUE;
        }
        if (d2 == 0.0) {
            return d3 < 0.0 ? -4.9E-324 : Double.MIN_VALUE;
        }
        if (d3 < d2 ^ (l2 = (l = Double.doubleToRawLongBits(d2)) & Long.MIN_VALUE) == 0L) {
            return Double.longBitsToDouble(l2 | (l & Long.MAX_VALUE) + 1L);
        }
        return Double.longBitsToDouble(l2 | (l & Long.MAX_VALUE) - 1L);
    }

    public static float nextAfter(float f2, double d2) {
        int n;
        int n2;
        if (Double.isNaN(f2) || Double.isNaN(d2)) {
            return Float.NaN;
        }
        if ((double)f2 == d2) {
            return (float)d2;
        }
        if (Float.isInfinite(f2)) {
            return f2 < 0.0f ? -3.4028235E38f : Float.MAX_VALUE;
        }
        if (f2 == 0.0f) {
            return d2 < 0.0 ? -1.4E-45f : Float.MIN_VALUE;
        }
        if (d2 < (double)f2 ^ (n2 = (n = Float.floatToIntBits(f2)) & Integer.MIN_VALUE) == 0) {
            return Float.intBitsToFloat(n2 | (n & Integer.MAX_VALUE) + 1);
        }
        return Float.intBitsToFloat(n2 | (n & Integer.MAX_VALUE) - 1);
    }

    public static double floor(double d2) {
        if (d2 != d2) {
            return d2;
        }
        if (d2 >= 4.503599627370496E15 || d2 <= -4.503599627370496E15) {
            return d2;
        }
        long l = (long)d2;
        if (d2 < 0.0 && (double)l != d2) {
            --l;
        }
        if (l == 0L) {
            return d2 * (double)l;
        }
        return l;
    }

    public static double ceil(double d2) {
        if (d2 != d2) {
            return d2;
        }
        double d3 = FastMathLite.floor(d2);
        if (d3 == d2) {
            return d3;
        }
        if ((d3 += 1.0) == 0.0) {
            return d2 * d3;
        }
        return d3;
    }

    public static double rint(double d2) {
        double d3 = FastMathLite.floor(d2);
        double d4 = d2 - d3;
        if (d4 > 0.5) {
            if (d3 == -1.0) {
                return -0.0;
            }
            return d3 + 1.0;
        }
        if (d4 < 0.5) {
            return d3;
        }
        long l = (long)d3;
        return (l & 1L) == 0L ? d3 : d3 + 1.0;
    }

    public static long round(double d2) {
        return (long)FastMathLite.floor(d2 + 0.5);
    }

    public static int round(float f2) {
        return (int)FastMathLite.floor(f2 + 0.5f);
    }

    public static int min(int n, int n2) {
        return n <= n2 ? n : n2;
    }

    public static long min(long l, long l2) {
        return l <= l2 ? l : l2;
    }

    public static float min(float f2, float f3) {
        if (f2 > f3) {
            return f3;
        }
        if (f2 < f3) {
            return f2;
        }
        if (f2 != f3) {
            return Float.NaN;
        }
        int n = Float.floatToRawIntBits(f2);
        if (n == Integer.MIN_VALUE) {
            return f2;
        }
        return f3;
    }

    public static double min(double d2, double d3) {
        if (d2 > d3) {
            return d3;
        }
        if (d2 < d3) {
            return d2;
        }
        if (d2 != d3) {
            return Double.NaN;
        }
        long l = Double.doubleToRawLongBits(d2);
        if (l == Long.MIN_VALUE) {
            return d2;
        }
        return d3;
    }

    public static int max(int n, int n2) {
        return n <= n2 ? n2 : n;
    }

    public static long max(long l, long l2) {
        return l <= l2 ? l2 : l;
    }

    public static float max(float f2, float f3) {
        if (f2 > f3) {
            return f2;
        }
        if (f2 < f3) {
            return f3;
        }
        if (f2 != f3) {
            return Float.NaN;
        }
        int n = Float.floatToRawIntBits(f2);
        if (n == Integer.MIN_VALUE) {
            return f3;
        }
        return f2;
    }

    public static double max(double d2, double d3) {
        if (d2 > d3) {
            return d2;
        }
        if (d2 < d3) {
            return d3;
        }
        if (d2 != d3) {
            return Double.NaN;
        }
        long l = Double.doubleToRawLongBits(d2);
        if (l == Long.MIN_VALUE) {
            return d3;
        }
        return d2;
    }

    public static double hypot(double d2, double d3) {
        int n;
        if (Double.isInfinite(d2) || Double.isInfinite(d3)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(d2) || Double.isNaN(d3)) {
            return Double.NaN;
        }
        int n2 = FastMathLite.getExponent(d2);
        if (n2 > (n = FastMathLite.getExponent(d3)) + 27) {
            return FastMathLite.abs(d2);
        }
        if (n > n2 + 27) {
            return FastMathLite.abs(d3);
        }
        int n3 = (n2 + n) / 2;
        double d4 = FastMathLite.scalb(d2, -n3);
        double d5 = FastMathLite.scalb(d3, -n3);
        double d6 = FastMathLite.sqrt(d4 * d4 + d5 * d5);
        return FastMathLite.scalb(d6, n3);
    }

    public static double IEEEremainder(double d2, double d3) {
        return StrictMath.IEEEremainder(d2, d3);
    }

    public static int toIntExact(long l) throws RuntimeException {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new RuntimeException("OVERFLOW");
        }
        return (int)l;
    }

    public static int incrementExact(int n) throws RuntimeException {
        if (n == Integer.MAX_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_ADDITION");
        }
        return n + 1;
    }

    public static long incrementExact(long l) throws RuntimeException {
        if (l == Long.MAX_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_ADDITION");
        }
        return l + 1L;
    }

    public static int decrementExact(int n) throws RuntimeException {
        if (n == Integer.MIN_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_SUBTRACTION");
        }
        return n - 1;
    }

    public static long decrementExact(long l) throws RuntimeException {
        if (l == Long.MIN_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_SUBTRACTION");
        }
        return l - 1L;
    }

    public static int addExact(int n, int n2) throws RuntimeException {
        int n3 = n + n2;
        if ((n ^ n2) >= 0 && (n3 ^ n2) < 0) {
            throw new RuntimeException("OVERFLOW_IN_ADDITION");
        }
        return n3;
    }

    public static long addExact(long l, long l2) throws RuntimeException {
        long l3 = l + l2;
        if ((l ^ l2) >= 0L && (l3 ^ l2) < 0L) {
            throw new RuntimeException("OVERFLOW_IN_ADDITION");
        }
        return l3;
    }

    public static int subtractExact(int n, int n2) {
        int n3 = n - n2;
        if ((n ^ n2) < 0 && (n3 ^ n2) >= 0) {
            throw new RuntimeException("OVERFLOW_IN_SUBTRACTION");
        }
        return n3;
    }

    public static long subtractExact(long l, long l2) {
        long l3 = l - l2;
        if ((l ^ l2) < 0L && (l3 ^ l2) >= 0L) {
            throw new RuntimeException("OVERFLOW_IN_SUBTRACTION");
        }
        return l3;
    }

    public static int multiplyExact(int n, int n2) {
        if (n2 > 0 && (n > Integer.MAX_VALUE / n2 || n < Integer.MIN_VALUE / n2) || n2 < -1 && (n > Integer.MIN_VALUE / n2 || n < Integer.MAX_VALUE / n2) || n2 == -1 && n == Integer.MIN_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_MULTIPLICATION");
        }
        return n * n2;
    }

    public static long multiplyExact(long l, long l2) {
        if (l2 > 0L && (l > Long.MAX_VALUE / l2 || l < Long.MIN_VALUE / l2) || l2 < -1L && (l > Long.MIN_VALUE / l2 || l < Long.MAX_VALUE / l2) || l2 == -1L && l == Long.MIN_VALUE) {
            throw new RuntimeException("OVERFLOW_IN_MULTIPLICATION");
        }
        return l * l2;
    }

    public static int floorDiv(int n, int n2) throws RuntimeException {
        if (n2 == 0) {
            throw new RuntimeException("ZERO_DENOMINATOR");
        }
        int n3 = n % n2;
        if ((n ^ n2) >= 0 || n3 == 0) {
            return n / n2;
        }
        return n / n2 - 1;
    }

    public static long floorDiv(long l, long l2) throws RuntimeException {
        if (l2 == 0L) {
            throw new RuntimeException("ZERO_DENOMINATOR");
        }
        long l3 = l % l2;
        if ((l ^ l2) >= 0L || l3 == 0L) {
            return l / l2;
        }
        return l / l2 - 1L;
    }

    public static int floorMod(int n, int n2) throws RuntimeException {
        if (n2 == 0) {
            throw new RuntimeException("ZERO_DENOMINATOR");
        }
        int n3 = n % n2;
        if ((n ^ n2) >= 0 || n3 == 0) {
            return n3;
        }
        return n2 + n3;
    }

    public static long floorMod(long l, long l2) {
        if (l2 == 0L) {
            throw new RuntimeException("ZERO_DENOMINATOR");
        }
        long l3 = l % l2;
        if ((l ^ l2) >= 0L || l3 == 0L) {
            return l3;
        }
        return l2 + l3;
    }

    public static double copySign(double d2, double d3) {
        long l;
        long l2 = Double.doubleToRawLongBits(d2);
        if ((l2 ^ (l = Double.doubleToRawLongBits(d3))) >= 0L) {
            return d2;
        }
        return -d2;
    }

    public static float copySign(float f2, float f3) {
        int n;
        int n2 = Float.floatToRawIntBits(f2);
        if ((n2 ^ (n = Float.floatToRawIntBits(f3))) >= 0) {
            return f2;
        }
        return -f2;
    }

    public static int getExponent(double d2) {
        return (int)(Double.doubleToRawLongBits(d2) >>> 52 & 0x7FFL) - 1023;
    }

    public static int getExponent(float f2) {
        return (Float.floatToRawIntBits(f2) >>> 23 & 0xFF) - 127;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    @NOBF
    private static class CodyWaite {
        private final int b;
        private final double a;
        private final double c;

        CodyWaite(double d2) {
            double d3;
            double d4;
            int n = (int)(d2 * 0.6366197723675814);
            while (true) {
                double d5 = (double)(-n) * 1.570796251296997;
                d4 = d2 + d5;
                d3 = -(d4 - d2 - d5);
                d5 = (double)(-n) * 7.549789948768648E-8;
                double d6 = d4;
                d4 = d5 + d6;
                d3 += -(d4 - d6 - d5);
                d5 = (double)(-n) * 6.123233995736766E-17;
                d6 = d4;
                d4 = d5 + d6;
                d3 += -(d4 - d6 - d5);
                if (d4 > 0.0) break;
                --n;
            }
            this.b = n;
            this.a = d4;
            this.c = d3;
        }

        int b() {
            return this.b;
        }

        double c() {
            return this.a;
        }

        double a() {
            return this.c;
        }
    }

    @NOBF
    private static class Split {
        public static final Split NAN = new Split(Double.NaN, 0.0);
        public static final Split POSITIVE_INFINITY = new Split(Double.POSITIVE_INFINITY, 0.0);
        public static final Split NEGATIVE_INFINITY = new Split(Double.NEGATIVE_INFINITY, 0.0);
        private final double b;
        private final double c;
        private final double a;

        Split(double d2) {
            this.b = d2;
            this.c = Double.longBitsToDouble(Double.doubleToRawLongBits(d2) & 0xFFFFFFFFF8000000L);
            this.a = d2 - this.c;
        }

        Split(double d2, double d3) {
            this(d2 == 0.0 ? (d3 == 0.0 && Double.doubleToRawLongBits(d2) == Long.MIN_VALUE ? -0.0 : d3) : d2 + d3, d2, d3);
        }

        Split(double d2, double d3, double d4) {
            this.b = d2;
            this.c = d3;
            this.a = d4;
        }

        public Split multiply(Split split) {
            Split split2 = new Split(this.b * split.b);
            double d2 = this.a * split.a - (split2.b - this.c * split.c - this.a * split.c - this.c * split.a);
            return new Split(split2.c, split2.a + d2);
        }

        public Split reciprocal() {
            double d2 = 1.0 / this.b;
            Split split = new Split(d2);
            Split split2 = this.multiply(split);
            double d3 = split2.c - 1.0 + split2.a;
            return Double.isNaN(d3) ? split : new Split(split.c, split.a - d3 / this.b);
        }

        private Split a(long l) {
            Split split = new Split(1.0);
            Split split2 = new Split(this.b, this.c, this.a);
            for (long k = l; k != 0L; k >>>= 1) {
                if ((k & 1L) != 0L) {
                    split = split.multiply(split2);
                }
                split2 = split2.multiply(split2);
            }
            if (Double.isNaN(split.b)) {
                if (Double.isNaN(this.b)) {
                    return NAN;
                }
                if (FastMathLite.abs(this.b) < 1.0) {
                    return new Split(FastMathLite.copySign(0.0, this.b), 0.0);
                }
                if (this.b < 0.0 && (l & 1L) == 1L) {
                    return NEGATIVE_INFINITY;
                }
                return POSITIVE_INFINITY;
            }
            return split;
        }
    }
}


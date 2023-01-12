/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import java.util.Random;

public class MathHelper {
    private static final float[] b = new float[65536];

    private MathHelper() {
    }

    public static float sqrt(float f2) {
        return (float)Math.sqrt(f2);
    }

    public static float sin(float f2) {
        return b[(int)(f2 * 10430.378f) & 0xFFFF];
    }

    public static float cos(float f2) {
        return b[(int)(f2 * 10430.378f + 16384.0f) & 0xFFFF];
    }

    public static float sin(double d2) {
        return b[(int)(d2 * 10430.3779296875) & 0xFFFF];
    }

    public static float cos(double d2) {
        return b[(int)(d2 * 10430.3779296875 + 16384.0) & 0xFFFF];
    }

    public static int floor(double d2) {
        int n = (int)d2;
        return d2 < (double)n ? n - 1 : n;
    }

    public static long floor_double_long(double d2) {
        long l = (long)d2;
        return d2 >= (double)l ? l : l - 1L;
    }

    public static int floor_float_int(float f2) {
        int n = (int)f2;
        return f2 >= (float)n ? n : n - 1;
    }

    public static int abs(int n) {
        if (n > 0) {
            return n;
        }
        return -n;
    }

    public static int log2(int n) {
        return 32 - Integer.numberOfLeadingZeros(n);
    }

    public static int getRandomNumberInRange(Random random, int n, int n2) {
        return n + random.nextInt(n2 - n + 1);
    }

    public static double max(double d2, double d3, double d4, double d5) {
        if (d2 > d3 && d2 > d4 && d2 > d5) {
            return d2;
        }
        if (d3 > d4 && d3 > d5) {
            return d3;
        }
        return Math.max(d4, d5);
    }

    public static int ceil(float f2) {
        int n = (int)f2;
        return f2 > (float)n ? n + 1 : n;
    }

    public static int clamp(int n, int n2, int n3) {
        return n > n3 ? n3 : Math.max(n, n2);
    }

    public static double denormalizeClamp(double d2, double d3, double d4) {
        return d4 < 0.0 ? d2 : (d4 > 1.0 ? d3 : d2 + (d3 - d2) * d4);
    }

    public static float denormalizeClamp(float f2, float f3, float f4) {
        return f4 < 0.0f ? f2 : (f4 > 1.0f ? f3 : f2 + (f3 - f2) * f4);
    }

    public static int log2nlz(int n) {
        if (n == 0) {
            return 0;
        }
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    static {
        for (int k = 0; k < 65536; ++k) {
            MathHelper.b[k] = (float)Math.sin((double)k * Math.PI * 2.0 / 65536.0);
        }
    }
}


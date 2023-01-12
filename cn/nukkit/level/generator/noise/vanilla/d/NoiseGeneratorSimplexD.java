/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.vanilla.d;

import cn.nukkit.math.NukkitRandom;

public class NoiseGeneratorSimplexD {
    public static final double SQRT_3 = Math.sqrt(3.0);
    private static final int[][] c = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    private static final double d = 0.5 * (SQRT_3 - 1.0);
    private static final double b = (3.0 - SQRT_3) / 6.0;
    private final int[] a = new int[512];
    public double xo;
    public double yo;
    public double zo;

    public NoiseGeneratorSimplexD() {
        this(new NukkitRandom(System.currentTimeMillis()));
    }

    public NoiseGeneratorSimplexD(NukkitRandom nukkitRandom) {
        this.xo = nukkitRandom.nextDouble() * 256.0;
        this.yo = nukkitRandom.nextDouble() * 256.0;
        this.zo = nukkitRandom.nextDouble() * 256.0;
        int n = 0;
        while (n < 256) {
            this.a[n] = n++;
        }
        for (int k = 0; k < 256; ++k) {
            int n2 = nukkitRandom.nextBoundedInt(256 - k) + k;
            int n3 = this.a[k];
            this.a[k] = this.a[n2];
            this.a[n2] = n3;
            this.a[k + 256] = this.a[k];
        }
    }

    private static int a(double d2) {
        return d2 > 0.0 ? (int)d2 : (int)d2 - 1;
    }

    private static double a(int[] nArray, double d2, double d3) {
        return (double)nArray[0] * d2 + (double)nArray[1] * d3;
    }

    public double getValue(double d2, double d3) {
        double d4;
        double d5;
        double d6;
        int n;
        int n2;
        double d7;
        double d8;
        double d9;
        int n3;
        double d10;
        double d11 = 0.5 * (SQRT_3 - 1.0);
        double d12 = (d2 + d3) * d11;
        int n4 = NoiseGeneratorSimplexD.a(d2 + d12);
        double d13 = (double)n4 - (d10 = (double)(n4 + (n3 = NoiseGeneratorSimplexD.a(d3 + d12))) * (d9 = (3.0 - SQRT_3) / 6.0));
        double d14 = d2 - d13;
        if (d14 > (d8 = d3 - (d7 = (double)n3 - d10))) {
            n2 = 1;
            n = 0;
        } else {
            n2 = 0;
            n = 1;
        }
        double d15 = d14 - (double)n2 + d9;
        double d16 = d8 - (double)n + d9;
        double d17 = d14 - 1.0 + 2.0 * d9;
        double d18 = d8 - 1.0 + 2.0 * d9;
        int n5 = n4 & 0xFF;
        int n6 = n3 & 0xFF;
        int n7 = this.a[n5 + this.a[n6]] % 12;
        int n8 = this.a[n5 + n2 + this.a[n6 + n]] % 12;
        int n9 = this.a[n5 + 1 + this.a[n6 + 1]] % 12;
        double d19 = 0.5 - d14 * d14 - d8 * d8;
        if (d19 < 0.0) {
            d6 = 0.0;
        } else {
            d19 *= d19;
            d6 = d19 * d19 * NoiseGeneratorSimplexD.a(c[n7], d14, d8);
        }
        double d20 = 0.5 - d15 * d15 - d16 * d16;
        if (d20 < 0.0) {
            d5 = 0.0;
        } else {
            d20 *= d20;
            d5 = d20 * d20 * NoiseGeneratorSimplexD.a(c[n8], d15, d16);
        }
        double d21 = 0.5 - d17 * d17 - d18 * d18;
        if (d21 < 0.0) {
            d4 = 0.0;
        } else {
            d21 *= d21;
            d4 = d21 * d21 * NoiseGeneratorSimplexD.a(c[n9], d17, d18);
        }
        return 70.0 * (d6 + d5 + d4);
    }

    public void add(double[] dArray, double d2, double d3, int n, int n2, double d4, double d5, double d6) {
        int n3 = 0;
        for (int k = 0; k < n2; ++k) {
            double d7 = (d3 + (double)k) * d5 + this.yo;
            for (int i2 = 0; i2 < n; ++i2) {
                int n4;
                double d8;
                double d9;
                double d10;
                int n5;
                int n6;
                double d11;
                double d12;
                int n7;
                double d13;
                double d14 = (d2 + (double)i2) * d4 + this.xo;
                double d15 = (d14 + d7) * d;
                int n8 = NoiseGeneratorSimplexD.a(d14 + d15);
                double d16 = (double)n8 - (d13 = (double)(n8 + (n7 = NoiseGeneratorSimplexD.a(d7 + d15))) * b);
                double d17 = d14 - d16;
                if (d17 > (d12 = d7 - (d11 = (double)n7 - d13))) {
                    n6 = 1;
                    n5 = 0;
                } else {
                    n6 = 0;
                    n5 = 1;
                }
                double d18 = d17 - (double)n6 + b;
                double d19 = d12 - (double)n5 + b;
                double d20 = d17 - 1.0 + 2.0 * b;
                double d21 = d12 - 1.0 + 2.0 * b;
                int n9 = n8 & 0xFF;
                int n10 = n7 & 0xFF;
                int n11 = this.a[n9 + this.a[n10]] % 12;
                int n12 = this.a[n9 + n6 + this.a[n10 + n5]] % 12;
                int n13 = this.a[n9 + 1 + this.a[n10 + 1]] % 12;
                double d22 = 0.5 - d17 * d17 - d12 * d12;
                if (d22 < 0.0) {
                    d10 = 0.0;
                } else {
                    d22 *= d22;
                    d10 = d22 * d22 * NoiseGeneratorSimplexD.a(c[n11], d17, d12);
                }
                double d23 = 0.5 - d18 * d18 - d19 * d19;
                if (d23 < 0.0) {
                    d9 = 0.0;
                } else {
                    d23 *= d23;
                    d9 = d23 * d23 * NoiseGeneratorSimplexD.a(c[n12], d18, d19);
                }
                double d24 = 0.5 - d20 * d20 - d21 * d21;
                if (d24 < 0.0) {
                    d8 = 0.0;
                } else {
                    d24 *= d24;
                    d8 = d24 * d24 * NoiseGeneratorSimplexD.a(c[n13], d20, d21);
                }
                int n14 = n4 = n3++;
                dArray[n14] = dArray[n14] + 70.0 * (d10 + d9 + d8) * d6;
            }
        }
    }
}


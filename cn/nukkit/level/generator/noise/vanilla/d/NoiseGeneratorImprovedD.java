/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.vanilla.d;

import cn.nukkit.math.NukkitRandom;

public class NoiseGeneratorImprovedD {
    private static final double[] d = new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
    private static final double[] e = new double[]{1.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0};
    private static final double[] c = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};
    private static final double[] b = new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
    private static final double[] f = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};
    private final int[] a = new int[512];
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public NoiseGeneratorImprovedD() {
        this(new NukkitRandom(System.currentTimeMillis()));
    }

    public NoiseGeneratorImprovedD(NukkitRandom nukkitRandom) {
        this.xCoord = nukkitRandom.nextDouble() * 256.0;
        this.yCoord = nukkitRandom.nextDouble() * 256.0;
        this.zCoord = nukkitRandom.nextDouble() * 256.0;
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

    public final double lerp(double d2, double d3, double d4) {
        return d3 + d2 * (d4 - d3);
    }

    public final double grad2(int n, double d2, double d3) {
        int n2 = n & 0xF;
        return b[n2] * d2 + f[n2] * d3;
    }

    public final double grad(int n, double d2, double d3, double d4) {
        int n2 = n & 0xF;
        return d[n2] * d2 + e[n2] * d3 + c[n2] * d4;
    }

    public void populateNoiseArray(double[] dArray, double d2, double d3, double d4, int n, int n2, int n3, double d5, double d6, double d7, double d8) {
        if (n2 == 1) {
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            double d9 = 0.0;
            double d10 = 0.0;
            int n8 = 0;
            double d11 = 1.0 / d8;
            for (int k = 0; k < n; ++k) {
                double d12 = d2 + (double)k * d5 + this.xCoord;
                int n9 = (int)d12;
                if (d12 < (double)n9) {
                    --n9;
                }
                int n10 = n9 & 0xFF;
                double d13 = (d12 -= (double)n9) * d12 * d12 * (d12 * (d12 * 6.0 - 15.0) + 10.0);
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n11;
                    double d14 = d4 + (double)i2 * d7 + this.zCoord;
                    int n12 = (int)d14;
                    if (d14 < (double)n12) {
                        --n12;
                    }
                    int n13 = n12 & 0xFF;
                    double d15 = (d14 -= (double)n12) * d14 * d14 * (d14 * (d14 * 6.0 - 15.0) + 10.0);
                    n4 = this.a[n10];
                    n5 = this.a[n4] + n13;
                    n6 = this.a[n10 + 1];
                    n7 = this.a[n6] + n13;
                    d9 = this.lerp(d13, this.grad2(this.a[n5], d12, d14), this.grad(this.a[n7], d12 - 1.0, 0.0, d14));
                    d10 = this.lerp(d13, this.grad(this.a[n5 + 1], d12, 0.0, d14 - 1.0), this.grad(this.a[n7 + 1], d12 - 1.0, 0.0, d14 - 1.0));
                    double d16 = this.lerp(d15, d9, d10);
                    int n14 = n11 = n8++;
                    dArray[n14] = dArray[n14] + d16 * d11;
                }
            }
        } else {
            int n15 = 0;
            double d17 = 1.0 / d8;
            int n16 = -1;
            int n17 = 0;
            int n18 = 0;
            int n19 = 0;
            int n20 = 0;
            int n21 = 0;
            int n22 = 0;
            double d18 = 0.0;
            double d19 = 0.0;
            double d20 = 0.0;
            double d21 = 0.0;
            for (int k = 0; k < n; ++k) {
                double d22 = d2 + (double)k * d5 + this.xCoord;
                int n23 = (int)d22;
                if (d22 < (double)n23) {
                    --n23;
                }
                int n24 = n23 & 0xFF;
                double d23 = (d22 -= (double)n23) * d22 * d22 * (d22 * (d22 * 6.0 - 15.0) + 10.0);
                for (int i3 = 0; i3 < n3; ++i3) {
                    double d24 = d4 + (double)i3 * d7 + this.zCoord;
                    int n25 = (int)d24;
                    if (d24 < (double)n25) {
                        --n25;
                    }
                    int n26 = n25 & 0xFF;
                    double d25 = (d24 -= (double)n25) * d24 * d24 * (d24 * (d24 * 6.0 - 15.0) + 10.0);
                    for (int i4 = 0; i4 < n2; ++i4) {
                        int n27;
                        double d26 = d3 + (double)i4 * d6 + this.yCoord;
                        int n28 = (int)d26;
                        if (d26 < (double)n28) {
                            --n28;
                        }
                        int n29 = n28 & 0xFF;
                        double d27 = (d26 -= (double)n28) * d26 * d26 * (d26 * (d26 * 6.0 - 15.0) + 10.0);
                        if (i4 == 0 || n29 != n16) {
                            n16 = n29;
                            n17 = this.a[n24] + n29;
                            n18 = this.a[n17] + n26;
                            n19 = this.a[n17 + 1] + n26;
                            n20 = this.a[n24 + 1] + n29;
                            n21 = this.a[n20] + n26;
                            n22 = this.a[n20 + 1] + n26;
                            d18 = this.lerp(d23, this.grad(this.a[n18], d22, d26, d24), this.grad(this.a[n21], d22 - 1.0, d26, d24));
                            d19 = this.lerp(d23, this.grad(this.a[n19], d22, d26 - 1.0, d24), this.grad(this.a[n22], d22 - 1.0, d26 - 1.0, d24));
                            d20 = this.lerp(d23, this.grad(this.a[n18 + 1], d22, d26, d24 - 1.0), this.grad(this.a[n21 + 1], d22 - 1.0, d26, d24 - 1.0));
                            d21 = this.lerp(d23, this.grad(this.a[n19 + 1], d22, d26 - 1.0, d24 - 1.0), this.grad(this.a[n22 + 1], d22 - 1.0, d26 - 1.0, d24 - 1.0));
                        }
                        double d28 = this.lerp(d27, d18, d19);
                        double d29 = this.lerp(d27, d20, d21);
                        double d30 = this.lerp(d25, d28, d29);
                        int n30 = n27 = n15++;
                        dArray[n30] = dArray[n30] + d30 * d17;
                    }
                }
            }
        }
    }
}


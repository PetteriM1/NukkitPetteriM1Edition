/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.vanilla.f;

import cn.nukkit.math.NukkitRandom;

public class NoiseGeneratorSimplexF {
    public static final float SQRT_3 = (float)Math.sqrt(3.0);
    private static final int[][] a = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    private static final float c = 0.5f * (SQRT_3 - 1.0f);
    private static final float b = (3.0f - SQRT_3) / 6.0f;
    private final int[] d = new int[512];
    public float xo;
    public float yo;
    public float zo;

    public NoiseGeneratorSimplexF() {
        this(new NukkitRandom(System.currentTimeMillis()));
    }

    public NoiseGeneratorSimplexF(NukkitRandom nukkitRandom) {
        this.xo = nukkitRandom.nextFloat() * 256.0f;
        this.yo = nukkitRandom.nextFloat() * 256.0f;
        this.zo = nukkitRandom.nextFloat() * 256.0f;
        int n = 0;
        while (n < 256) {
            this.d[n] = n++;
        }
        for (int k = 0; k < 256; ++k) {
            int n2 = nukkitRandom.nextBoundedInt(256 - k) + k;
            int n3 = this.d[k];
            this.d[k] = this.d[n2];
            this.d[n2] = n3;
            this.d[k + 256] = this.d[k];
        }
    }

    private static int a(float f2) {
        return f2 > 0.0f ? (int)f2 : (int)f2 - 1;
    }

    private static float a(int[] nArray, float f2, float f3) {
        return (float)nArray[0] * f2 + (float)nArray[1] * f3;
    }

    public float getValue(float f2, float f3) {
        float f4;
        float f5;
        float f6;
        int n;
        int n2;
        float f7;
        float f8;
        float f9;
        int n3;
        float f10;
        float f11 = 0.5f * (SQRT_3 - 1.0f);
        float f12 = (f2 + f3) * f11;
        int n4 = NoiseGeneratorSimplexF.a(f2 + f12);
        float f13 = (float)n4 - (f10 = (float)(n4 + (n3 = NoiseGeneratorSimplexF.a(f3 + f12))) * (f9 = (3.0f - SQRT_3) / 6.0f));
        float f14 = f2 - f13;
        if (f14 > (f8 = f3 - (f7 = (float)n3 - f10))) {
            n2 = 1;
            n = 0;
        } else {
            n2 = 0;
            n = 1;
        }
        float f15 = f14 - (float)n2 + f9;
        float f16 = f8 - (float)n + f9;
        float f17 = f14 - 1.0f + 2.0f * f9;
        float f18 = f8 - 1.0f + 2.0f * f9;
        int n5 = n4 & 0xFF;
        int n6 = n3 & 0xFF;
        int n7 = this.d[n5 + this.d[n6]] % 12;
        int n8 = this.d[n5 + n2 + this.d[n6 + n]] % 12;
        int n9 = this.d[n5 + 1 + this.d[n6 + 1]] % 12;
        float f19 = 0.5f - f14 * f14 - f8 * f8;
        if (f19 < 0.0f) {
            f6 = 0.0f;
        } else {
            f19 *= f19;
            f6 = f19 * f19 * NoiseGeneratorSimplexF.a(a[n7], f14, f8);
        }
        float f20 = 0.5f - f15 * f15 - f16 * f16;
        if (f20 < 0.0f) {
            f5 = 0.0f;
        } else {
            f20 *= f20;
            f5 = f20 * f20 * NoiseGeneratorSimplexF.a(a[n8], f15, f16);
        }
        float f21 = 0.5f - f17 * f17 - f18 * f18;
        if (f21 < 0.0f) {
            f4 = 0.0f;
        } else {
            f21 *= f21;
            f4 = f21 * f21 * NoiseGeneratorSimplexF.a(a[n9], f17, f18);
        }
        return 70.0f * (f6 + f5 + f4);
    }

    public void add(float[] fArray, float f2, float f3, int n, int n2, float f4, float f5, float f6) {
        int n3 = 0;
        for (int k = 0; k < n2; ++k) {
            float f7 = (f3 + (float)k) * f5 + this.yo;
            for (int i2 = 0; i2 < n; ++i2) {
                int n4;
                float f8;
                float f9;
                float f10;
                int n5;
                int n6;
                float f11;
                float f12;
                int n7;
                float f13;
                float f14 = (f2 + (float)i2) * f4 + this.xo;
                float f15 = (f14 + f7) * c;
                int n8 = NoiseGeneratorSimplexF.a(f14 + f15);
                float f16 = (float)n8 - (f13 = (float)(n8 + (n7 = NoiseGeneratorSimplexF.a(f7 + f15))) * b);
                float f17 = f14 - f16;
                if (f17 > (f12 = f7 - (f11 = (float)n7 - f13))) {
                    n6 = 1;
                    n5 = 0;
                } else {
                    n6 = 0;
                    n5 = 1;
                }
                float f18 = f17 - (float)n6 + b;
                float f19 = f12 - (float)n5 + b;
                float f20 = f17 - 1.0f + 2.0f * b;
                float f21 = f12 - 1.0f + 2.0f * b;
                int n9 = n8 & 0xFF;
                int n10 = n7 & 0xFF;
                int n11 = this.d[n9 + this.d[n10]] % 12;
                int n12 = this.d[n9 + n6 + this.d[n10 + n5]] % 12;
                int n13 = this.d[n9 + 1 + this.d[n10 + 1]] % 12;
                float f22 = 0.5f - f17 * f17 - f12 * f12;
                if (f22 < 0.0f) {
                    f10 = 0.0f;
                } else {
                    f22 *= f22;
                    f10 = f22 * f22 * NoiseGeneratorSimplexF.a(a[n11], f17, f12);
                }
                float f23 = 0.5f - f18 * f18 - f19 * f19;
                if (f23 < 0.0f) {
                    f9 = 0.0f;
                } else {
                    f23 *= f23;
                    f9 = f23 * f23 * NoiseGeneratorSimplexF.a(a[n12], f18, f19);
                }
                float f24 = 0.5f - f20 * f20 - f21 * f21;
                if (f24 < 0.0f) {
                    f8 = 0.0f;
                } else {
                    f24 *= f24;
                    f8 = f24 * f24 * NoiseGeneratorSimplexF.a(a[n13], f20, f21);
                }
                int n14 = n4 = n3++;
                fArray[n14] = fArray[n14] + 70.0f * (f10 + f9 + f8) * f6;
            }
        }
    }
}


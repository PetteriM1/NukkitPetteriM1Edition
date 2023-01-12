/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.vanilla.f;

import cn.nukkit.math.NukkitRandom;

public class NoiseGeneratorImprovedF {
    private static final float[] c = new float[]{1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f};
    private static final float[] e = new float[]{1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f};
    private static final float[] f = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f};
    private static final float[] a = new float[]{1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f};
    private static final float[] b = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f};
    private final int[] d = new int[512];
    public float xCoord;
    public float yCoord;
    public float zCoord;

    public NoiseGeneratorImprovedF() {
        this(new NukkitRandom(System.currentTimeMillis()));
    }

    public NoiseGeneratorImprovedF(NukkitRandom nukkitRandom) {
        this.xCoord = nukkitRandom.nextFloat() * 256.0f;
        this.yCoord = nukkitRandom.nextFloat() * 256.0f;
        this.zCoord = nukkitRandom.nextFloat() * 256.0f;
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

    public final float lerp(float f2, float f3, float f4) {
        return f3 + f2 * (f4 - f3);
    }

    public final float grad2(int n, float f2, float f3) {
        int n2 = n & 0xF;
        return a[n2] * f2 + b[n2] * f3;
    }

    public final float grad(int n, float f2, float f3, float f4) {
        int n2 = n & 0xF;
        return c[n2] * f2 + e[n2] * f3 + f[n2] * f4;
    }

    public void populateNoiseArray(float[] fArray, float f2, float f3, float f4, int n, int n2, int n3, float f5, float f6, float f7, float f8) {
        if (n2 == 1) {
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            float f9 = 0.0f;
            float f10 = 0.0f;
            int n8 = 0;
            float f11 = 1.0f / f8;
            for (int k = 0; k < n; ++k) {
                float f12 = f2 + (float)k * f5 + this.xCoord;
                int n9 = (int)f12;
                if (f12 < (float)n9) {
                    --n9;
                }
                int n10 = n9 & 0xFF;
                float f13 = (f12 -= (float)n9) * f12 * f12 * (f12 * (f12 * 6.0f - 15.0f) + 10.0f);
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n11;
                    float f14 = f4 + (float)i2 * f7 + this.zCoord;
                    int n12 = (int)f14;
                    if (f14 < (float)n12) {
                        --n12;
                    }
                    int n13 = n12 & 0xFF;
                    float f15 = (f14 -= (float)n12) * f14 * f14 * (f14 * (f14 * 6.0f - 15.0f) + 10.0f);
                    n4 = this.d[n10];
                    n5 = this.d[n4] + n13;
                    n6 = this.d[n10 + 1];
                    n7 = this.d[n6] + n13;
                    f9 = this.lerp(f13, this.grad2(this.d[n5], f12, f14), this.grad(this.d[n7], f12 - 1.0f, 0.0f, f14));
                    f10 = this.lerp(f13, this.grad(this.d[n5 + 1], f12, 0.0f, f14 - 1.0f), this.grad(this.d[n7 + 1], f12 - 1.0f, 0.0f, f14 - 1.0f));
                    float f16 = this.lerp(f15, f9, f10);
                    int n14 = n11 = n8++;
                    fArray[n14] = fArray[n14] + f16 * f11;
                }
            }
        } else {
            int n15 = 0;
            float f17 = 1.0f / f8;
            int n16 = -1;
            int n17 = 0;
            int n18 = 0;
            int n19 = 0;
            int n20 = 0;
            int n21 = 0;
            int n22 = 0;
            float f18 = 0.0f;
            float f19 = 0.0f;
            float f20 = 0.0f;
            float f21 = 0.0f;
            for (int k = 0; k < n; ++k) {
                float f22 = f2 + (float)k * f5 + this.xCoord;
                int n23 = (int)f22;
                if (f22 < (float)n23) {
                    --n23;
                }
                int n24 = n23 & 0xFF;
                float f23 = (f22 -= (float)n23) * f22 * f22 * (f22 * (f22 * 6.0f - 15.0f) + 10.0f);
                for (int i3 = 0; i3 < n3; ++i3) {
                    float f24 = f4 + (float)i3 * f7 + this.zCoord;
                    int n25 = (int)f24;
                    if (f24 < (float)n25) {
                        --n25;
                    }
                    int n26 = n25 & 0xFF;
                    float f25 = (f24 -= (float)n25) * f24 * f24 * (f24 * (f24 * 6.0f - 15.0f) + 10.0f);
                    for (int i4 = 0; i4 < n2; ++i4) {
                        int n27;
                        float f26 = f3 + (float)i4 * f6 + this.yCoord;
                        int n28 = (int)f26;
                        if (f26 < (float)n28) {
                            --n28;
                        }
                        int n29 = n28 & 0xFF;
                        float f27 = (f26 -= (float)n28) * f26 * f26 * (f26 * (f26 * 6.0f - 15.0f) + 10.0f);
                        if (i4 == 0 || n29 != n16) {
                            n16 = n29;
                            n17 = this.d[n24] + n29;
                            n18 = this.d[n17] + n26;
                            n19 = this.d[n17 + 1] + n26;
                            n20 = this.d[n24 + 1] + n29;
                            n21 = this.d[n20] + n26;
                            n22 = this.d[n20 + 1] + n26;
                            f18 = this.lerp(f23, this.grad(this.d[n18], f22, f26, f24), this.grad(this.d[n21], f22 - 1.0f, f26, f24));
                            f19 = this.lerp(f23, this.grad(this.d[n19], f22, f26 - 1.0f, f24), this.grad(this.d[n22], f22 - 1.0f, f26 - 1.0f, f24));
                            f20 = this.lerp(f23, this.grad(this.d[n18 + 1], f22, f26, f24 - 1.0f), this.grad(this.d[n21 + 1], f22 - 1.0f, f26, f24 - 1.0f));
                            f21 = this.lerp(f23, this.grad(this.d[n19 + 1], f22, f26 - 1.0f, f24 - 1.0f), this.grad(this.d[n22 + 1], f22 - 1.0f, f26 - 1.0f, f24 - 1.0f));
                        }
                        float f28 = this.lerp(f27, f18, f19);
                        float f29 = this.lerp(f27, f20, f21);
                        float f30 = this.lerp(f25, f28, f29);
                        int n30 = n27 = n15++;
                        fArray[n30] = fArray[n30] + f30 * f17;
                    }
                }
            }
        }
    }
}


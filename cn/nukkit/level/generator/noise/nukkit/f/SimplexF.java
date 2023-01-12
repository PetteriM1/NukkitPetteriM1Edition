/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.nukkit.f;

import cn.nukkit.level.generator.noise.nukkit.f.PerlinF;
import cn.nukkit.math.NukkitRandom;

public class SimplexF
extends PerlinF {
    protected static float SQRT_3;
    protected static float SQRT_5;
    protected static float F2;
    protected static float G2;
    protected static float G22;
    protected static float F3;
    protected static float G3;
    protected static float F4;
    protected static float G4;
    protected static float G42;
    protected static float G43;
    protected static float G44;
    public static final int[][] grad3;
    protected final float offsetW;

    public SimplexF(NukkitRandom nukkitRandom, float f2, float f3) {
        super(nukkitRandom, f2, f3);
        this.offsetW = nukkitRandom.nextFloat() * 256.0f;
        SQRT_3 = (float)Math.sqrt(3.0);
        SQRT_5 = (float)Math.sqrt(5.0);
        F2 = 0.5f * (SQRT_3 - 1.0f);
        G2 = (3.0f - SQRT_3) / 6.0f;
        G22 = G2 * 2.0f - 1.0f;
        F3 = 0.33333334f;
        G3 = 0.16666667f;
        F4 = (SQRT_5 - 1.0f) / 4.0f;
        G4 = (5.0f - SQRT_5) / 20.0f;
        G42 = G4 * 2.0f;
        G43 = G4 * 3.0f;
        G44 = G4 * 4.0f - 1.0f;
    }

    public SimplexF(NukkitRandom nukkitRandom, float f2, float f3, float f4) {
        super(nukkitRandom, f2, f3, f4);
        this.offsetW = nukkitRandom.nextFloat() * 256.0f;
        SQRT_3 = (float)Math.sqrt(3.0);
        SQRT_5 = (float)Math.sqrt(5.0);
        F2 = 0.5f * (SQRT_3 - 1.0f);
        G2 = (3.0f - SQRT_3) / 6.0f;
        G22 = G2 * 2.0f - 1.0f;
        F3 = 0.33333334f;
        G3 = 0.16666667f;
        F4 = (SQRT_5 - 1.0f) / 4.0f;
        G4 = (5.0f - SQRT_5) / 20.0f;
        G42 = G4 * 2.0f;
        G43 = G4 * 3.0f;
        G44 = G4 * 4.0f - 1.0f;
    }

    protected static float dot2D(int[] nArray, float f2, float f3) {
        return (float)nArray[0] * f2 + (float)nArray[1] * f3;
    }

    protected static float dot3D(int[] nArray, float f2, float f3, float f4) {
        return (float)nArray[0] * f2 + (float)nArray[1] * f3 + (float)nArray[2] * f4;
    }

    protected static float dot4D(int[] nArray, float f2, float f3, float f4, float f5) {
        return (float)nArray[0] * f2 + (float)nArray[1] * f3 + (float)nArray[2] * f4 + (float)nArray[3] * f5;
    }

    @Override
    public float getNoise3D(float f2, float f3, float f4) {
        float f5;
        float f6;
        float f7;
        float f8 = ((f2 += this.offsetX) + (f3 += this.offsetY) + (f4 += this.offsetZ)) * F3;
        int n = (int)(f2 + f8);
        int n2 = (int)(f3 + f8);
        int n3 = (int)(f4 + f8);
        float f9 = (float)(n + n2 + n3) * G3;
        float f10 = f2 - ((float)n - f9);
        float f11 = f3 - ((float)n2 - f9);
        float f12 = f4 - ((float)n3 - f9);
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        if (f10 >= f11) {
            if (f11 >= f12) {
                n4 = 1;
                n5 = 0;
                n6 = 0;
                n7 = 1;
                n8 = 1;
                n9 = 0;
            } else if (f10 >= f12) {
                n4 = 1;
                n5 = 0;
                n6 = 0;
                n7 = 1;
                n8 = 0;
                n9 = 1;
            } else {
                n4 = 0;
                n5 = 0;
                n6 = 1;
                n7 = 1;
                n8 = 0;
                n9 = 1;
            }
        } else if (f11 < f12) {
            n4 = 0;
            n5 = 0;
            n6 = 1;
            n7 = 0;
            n8 = 1;
            n9 = 1;
        } else if (f10 < f12) {
            n4 = 0;
            n5 = 1;
            n6 = 0;
            n7 = 0;
            n8 = 1;
            n9 = 1;
        } else {
            n4 = 0;
            n5 = 1;
            n6 = 0;
            n7 = 1;
            n8 = 1;
            n9 = 0;
        }
        float f13 = f10 - (float)n4 + G3;
        float f14 = f11 - (float)n5 + G3;
        float f15 = f12 - (float)n6 + G3;
        float f16 = f10 - (float)n7 + 2.0f * G3;
        float f17 = f11 - (float)n8 + 2.0f * G3;
        float f18 = f12 - (float)n9 + 2.0f * G3;
        float f19 = f10 - 1.0f + 3.0f * G3;
        float f20 = f11 - 1.0f + 3.0f * G3;
        float f21 = f12 - 1.0f + 3.0f * G3;
        int n10 = n & 0xFF;
        int n11 = n2 & 0xFF;
        int n12 = n3 & 0xFF;
        float f22 = 0.0f;
        float f23 = 0.6f - f10 * f10 - f11 * f11 - f12 * f12;
        if (f23 > 0.0f) {
            int[] nArray = grad3[this.perm[n10 + this.perm[n11 + this.perm[n12]]] % 12];
            f22 += f23 * f23 * f23 * f23 * ((float)nArray[0] * f10 + (float)nArray[1] * f11 + (float)nArray[2] * f12);
        }
        if ((f7 = 0.6f - f13 * f13 - f14 * f14 - f15 * f15) > 0.0f) {
            int[] nArray = grad3[this.perm[n10 + n4 + this.perm[n11 + n5 + this.perm[n12 + n6]]] % 12];
            f22 += f7 * f7 * f7 * f7 * ((float)nArray[0] * f13 + (float)nArray[1] * f14 + (float)nArray[2] * f15);
        }
        if ((f6 = 0.6f - f16 * f16 - f17 * f17 - f18 * f18) > 0.0f) {
            int[] nArray = grad3[this.perm[n10 + n7 + this.perm[n11 + n8 + this.perm[n12 + n9]]] % 12];
            f22 += f6 * f6 * f6 * f6 * ((float)nArray[0] * f16 + (float)nArray[1] * f17 + (float)nArray[2] * f18);
        }
        if ((f5 = 0.6f - f19 * f19 - f20 * f20 - f21 * f21) > 0.0f) {
            int[] nArray = grad3[this.perm[n10 + 1 + this.perm[n11 + 1 + this.perm[n12 + 1]]] % 12];
            f22 += f5 * f5 * f5 * f5 * ((float)nArray[0] * f19 + (float)nArray[1] * f20 + (float)nArray[2] * f21);
        }
        return 32.0f * f22;
    }

    @Override
    public float getNoise2D(float f2, float f3) {
        float f4;
        float f5;
        float f6 = ((f2 += this.offsetX) + (f3 += this.offsetY)) * F2;
        int n = (int)(f2 + f6);
        int n2 = (int)(f3 + f6);
        float f7 = (float)(n + n2) * G2;
        float f8 = f2 - ((float)n - f7);
        float f9 = f3 - ((float)n2 - f7);
        int n3 = 0;
        int n4 = 0;
        if (f8 > f9) {
            n3 = 1;
            n4 = 0;
        } else {
            n3 = 0;
            n4 = 1;
        }
        float f10 = f8 - (float)n3 + G2;
        float f11 = f9 - (float)n4 + G2;
        float f12 = f8 + G22;
        float f13 = f9 + G22;
        int n5 = n & 0xFF;
        int n6 = n2 & 0xFF;
        float f14 = 0.0f;
        float f15 = 0.5f - f8 * f8 - f9 * f9;
        if (f15 > 0.0f) {
            int[] nArray = grad3[this.perm[n5 + this.perm[n6]] % 12];
            f14 += f15 * f15 * f15 * f15 * ((float)nArray[0] * f8 + (float)nArray[1] * f9);
        }
        if ((f5 = 0.5f - f10 * f10 - f11 * f11) > 0.0f) {
            int[] nArray = grad3[this.perm[n5 + n3 + this.perm[n6 + n4]] % 12];
            f14 += f5 * f5 * f5 * f5 * ((float)nArray[0] * f10 + (float)nArray[1] * f11);
        }
        if ((f4 = 0.5f - f12 * f12 - f13 * f13) > 0.0f) {
            int[] nArray = grad3[this.perm[n5 + 1 + this.perm[n6 + 1]] % 12];
            f14 += f4 * f4 * f4 * f4 * ((float)nArray[0] * f12 + (float)nArray[1] * f13);
        }
        return 70.0f * f14;
    }

    static {
        grad3 = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    }
}


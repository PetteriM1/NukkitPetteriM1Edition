/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.nukkit.d;

import cn.nukkit.level.generator.noise.nukkit.d.PerlinD;
import cn.nukkit.math.NukkitRandom;

public class SimplexD
extends PerlinD {
    protected static double SQRT_3;
    protected static double SQRT_5;
    protected static double F2;
    protected static double G2;
    protected static double G22;
    protected static double F3;
    protected static double G3;
    protected static double F4;
    protected static double G4;
    protected static double G42;
    protected static double G43;
    protected static double G44;
    public static final int[][] grad3;
    protected final double offsetW;

    public SimplexD(NukkitRandom nukkitRandom, double d2, double d3) {
        super(nukkitRandom, d2, d3);
        this.offsetW = nukkitRandom.nextDouble() * 256.0;
        SQRT_3 = Math.sqrt(3.0);
        SQRT_5 = Math.sqrt(5.0);
        F2 = 0.5 * (SQRT_3 - 1.0);
        G2 = (3.0 - SQRT_3) / 6.0;
        G22 = G2 * 2.0 - 1.0;
        F3 = 0.3333333333333333;
        G3 = 0.16666666666666666;
        F4 = (SQRT_5 - 1.0) / 4.0;
        G4 = (5.0 - SQRT_5) / 20.0;
        G42 = G4 * 2.0;
        G43 = G4 * 3.0;
        G44 = G4 * 4.0 - 1.0;
    }

    public SimplexD(NukkitRandom nukkitRandom, double d2, double d3, double d4) {
        super(nukkitRandom, d2, d3, d4);
        this.offsetW = nukkitRandom.nextDouble() * 256.0;
        SQRT_3 = Math.sqrt(3.0);
        SQRT_5 = Math.sqrt(5.0);
        F2 = 0.5 * (SQRT_3 - 1.0);
        G2 = (3.0 - SQRT_3) / 6.0;
        G22 = G2 * 2.0 - 1.0;
        F3 = 0.3333333333333333;
        G3 = 0.16666666666666666;
        F4 = (SQRT_5 - 1.0) / 4.0;
        G4 = (5.0 - SQRT_5) / 20.0;
        G42 = G4 * 2.0;
        G43 = G4 * 3.0;
        G44 = G4 * 4.0 - 1.0;
    }

    protected static double dot2D(int[] nArray, double d2, double d3) {
        return (double)nArray[0] * d2 + (double)nArray[1] * d3;
    }

    protected static double dot3D(int[] nArray, double d2, double d3, double d4) {
        return (double)nArray[0] * d2 + (double)nArray[1] * d3 + (double)nArray[2] * d4;
    }

    protected static double dot4D(int[] nArray, double d2, double d3, double d4, double d5) {
        return (double)nArray[0] * d2 + (double)nArray[1] * d3 + (double)nArray[2] * d4 + (double)nArray[3] * d5;
    }

    @Override
    public double getNoise3D(double d2, double d3, double d4) {
        double d5;
        double d6;
        double d7;
        double d8 = ((d2 += this.offsetX) + (d3 += this.offsetY) + (d4 += this.offsetZ)) * F3;
        int n = (int)(d2 + d8);
        int n2 = (int)(d3 + d8);
        int n3 = (int)(d4 + d8);
        double d9 = (double)(n + n2 + n3) * G3;
        double d10 = d2 - ((double)n - d9);
        double d11 = d3 - ((double)n2 - d9);
        double d12 = d4 - ((double)n3 - d9);
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        if (d10 >= d11) {
            if (d11 >= d12) {
                n4 = 1;
                n5 = 0;
                n6 = 0;
                n7 = 1;
                n8 = 1;
                n9 = 0;
            } else if (d10 >= d12) {
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
        } else if (d11 < d12) {
            n4 = 0;
            n5 = 0;
            n6 = 1;
            n7 = 0;
            n8 = 1;
            n9 = 1;
        } else if (d10 < d12) {
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
        double d13 = d10 - (double)n4 + G3;
        double d14 = d11 - (double)n5 + G3;
        double d15 = d12 - (double)n6 + G3;
        double d16 = d10 - (double)n7 + 2.0 * G3;
        double d17 = d11 - (double)n8 + 2.0 * G3;
        double d18 = d12 - (double)n9 + 2.0 * G3;
        double d19 = d10 - 1.0 + 3.0 * G3;
        double d20 = d11 - 1.0 + 3.0 * G3;
        double d21 = d12 - 1.0 + 3.0 * G3;
        int n10 = n & 0xFF;
        int n11 = n2 & 0xFF;
        int n12 = n3 & 0xFF;
        double d22 = 0.0;
        double d23 = 0.6 - d10 * d10 - d11 * d11 - d12 * d12;
        if (d23 > 0.0) {
            int[] nArray = grad3[this.perm[n10 + this.perm[n11 + this.perm[n12]]] % 12];
            d22 += d23 * d23 * d23 * d23 * ((double)nArray[0] * d10 + (double)nArray[1] * d11 + (double)nArray[2] * d12);
        }
        if ((d7 = 0.6 - d13 * d13 - d14 * d14 - d15 * d15) > 0.0) {
            int[] nArray = grad3[this.perm[n10 + n4 + this.perm[n11 + n5 + this.perm[n12 + n6]]] % 12];
            d22 += d7 * d7 * d7 * d7 * ((double)nArray[0] * d13 + (double)nArray[1] * d14 + (double)nArray[2] * d15);
        }
        if ((d6 = 0.6 - d16 * d16 - d17 * d17 - d18 * d18) > 0.0) {
            int[] nArray = grad3[this.perm[n10 + n7 + this.perm[n11 + n8 + this.perm[n12 + n9]]] % 12];
            d22 += d6 * d6 * d6 * d6 * ((double)nArray[0] * d16 + (double)nArray[1] * d17 + (double)nArray[2] * d18);
        }
        if ((d5 = 0.6 - d19 * d19 - d20 * d20 - d21 * d21) > 0.0) {
            int[] nArray = grad3[this.perm[n10 + 1 + this.perm[n11 + 1 + this.perm[n12 + 1]]] % 12];
            d22 += d5 * d5 * d5 * d5 * ((double)nArray[0] * d19 + (double)nArray[1] * d20 + (double)nArray[2] * d21);
        }
        return 32.0 * d22;
    }

    @Override
    public double getNoise2D(double d2, double d3) {
        double d4;
        double d5;
        double d6 = ((d2 += this.offsetX) + (d3 += this.offsetY)) * F2;
        int n = (int)(d2 + d6);
        int n2 = (int)(d3 + d6);
        double d7 = (double)(n + n2) * G2;
        double d8 = d2 - ((double)n - d7);
        double d9 = d3 - ((double)n2 - d7);
        int n3 = 0;
        int n4 = 0;
        if (d8 > d9) {
            n3 = 1;
            n4 = 0;
        } else {
            n3 = 0;
            n4 = 1;
        }
        double d10 = d8 - (double)n3 + G2;
        double d11 = d9 - (double)n4 + G2;
        double d12 = d8 + G22;
        double d13 = d9 + G22;
        int n5 = n & 0xFF;
        int n6 = n2 & 0xFF;
        double d14 = 0.0;
        double d15 = 0.5 - d8 * d8 - d9 * d9;
        if (d15 > 0.0) {
            int[] nArray = grad3[this.perm[n5 + this.perm[n6]] % 12];
            d14 += d15 * d15 * d15 * d15 * ((double)nArray[0] * d8 + (double)nArray[1] * d9);
        }
        if ((d5 = 0.5 - d10 * d10 - d11 * d11) > 0.0) {
            int[] nArray = grad3[this.perm[n5 + n3 + this.perm[n6 + n4]] % 12];
            d14 += d5 * d5 * d5 * d5 * ((double)nArray[0] * d10 + (double)nArray[1] * d11);
        }
        if ((d4 = 0.5 - d12 * d12 - d13 * d13) > 0.0) {
            int[] nArray = grad3[this.perm[n5 + 1 + this.perm[n6 + 1]] % 12];
            d14 += d4 * d4 * d4 * d4 * ((double)nArray[0] * d12 + (double)nArray[1] * d13);
        }
        return 70.0 * d14;
    }

    static {
        grad3 = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    }
}


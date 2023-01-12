/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.nukkit.d;

public abstract class NoiseD {
    protected int[] perm;
    protected double offsetX = 0.0;
    protected double offsetY = 0.0;
    protected double offsetZ = 0.0;
    protected double octaves = 8.0;
    protected double persistence;
    protected double expansion;

    public static int floor(double d2) {
        return d2 >= 0.0 ? (int)d2 : (int)(d2 - 1.0);
    }

    public static double fade(double d2) {
        return d2 * d2 * d2 * (d2 * (d2 * 6.0 - 15.0) + 10.0);
    }

    public static double lerp(double d2, double d3, double d4) {
        return d3 + d2 * (d4 - d3);
    }

    public static double linearLerp(double d2, double d3, double d4, double d5, double d6) {
        return (d4 - d2) / (d4 - d3) * d5 + (d2 - d3) / (d4 - d3) * d6;
    }

    public static double bilinearLerp(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
        double d12 = (d9 - d2) / (d9 - d8);
        double d13 = (d2 - d8) / (d9 - d8);
        return (d11 - d3) / (d11 - d10) * (d12 * d4 + d13 * d6) + (d3 - d10) / (d11 - d10) * (d12 * d5 + d13 * d7);
    }

    public static double trilinearLerp(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15, double d16, double d17, double d18) {
        double d19 = (d14 - d2) / (d14 - d13);
        double d20 = (d2 - d13) / (d14 - d13);
        double d21 = (d16 - d3) / (d16 - d15);
        double d22 = (d3 - d15) / (d16 - d15);
        return (d18 - d4) / (d18 - d17) * (d21 * (d19 * d5 + d20 * d9) + d22 * (d19 * d6 + d20 * d10)) + (d4 - d17) / (d18 - d17) * (d21 * (d19 * d7 + d20 * d11) + d22 * (d19 * d8 + d20 * d12));
    }

    public static double grad(int n, double d2, double d3, double d4) {
        double d5;
        double d6 = d5 = (n &= 0xF) < 8 ? d2 : d3;
        double d7 = n < 4 ? d3 : (n == 12 || n == 14 ? d2 : d4);
        return ((n & 1) == 0 ? d5 : -d5) + ((n & 2) == 0 ? d7 : -d7);
    }

    public abstract double getNoise2D(double var1, double var3);

    public abstract double getNoise3D(double var1, double var3, double var5);

    public double noise2D(double d2, double d3) {
        return this.noise2D(d2, d3, false);
    }

    public double noise2D(double d2, double d3, boolean bl) {
        double d4 = 0.0;
        double d5 = 1.0;
        double d6 = 1.0;
        double d7 = 0.0;
        d2 *= this.expansion;
        d3 *= this.expansion;
        int n = 0;
        while ((double)n < this.octaves) {
            d4 += this.getNoise2D(d2 * d6, d3 * d6) * d5;
            d7 += d5;
            d6 *= 2.0;
            d5 *= this.persistence;
            ++n;
        }
        if (bl) {
            d4 /= d7;
        }
        return d4;
    }

    public double noise3D(double d2, double d3, double d4) {
        return this.noise3D(d2, d3, d4, false);
    }

    public double noise3D(double d2, double d3, double d4, boolean bl) {
        double d5 = 0.0;
        double d6 = 1.0;
        double d7 = 1.0;
        double d8 = 0.0;
        d2 *= this.expansion;
        d3 *= this.expansion;
        d4 *= this.expansion;
        int n = 0;
        while ((double)n < this.octaves) {
            d5 += this.getNoise3D(d2 * d7, d3 * d7, d4 * d7) * d6;
            d8 += d6;
            d7 *= 2.0;
            d6 *= this.persistence;
            ++n;
        }
        if (bl) {
            d5 /= d8;
        }
        return d5;
    }

    public void setOffset(double d2, double d3, double d4) {
        this.offsetX = d2;
        this.offsetY = d3;
        this.offsetZ = d4;
    }
}


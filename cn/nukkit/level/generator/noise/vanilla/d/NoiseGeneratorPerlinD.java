/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.vanilla.d;

import cn.nukkit.level.generator.noise.vanilla.d.NoiseGeneratorSimplexD;
import cn.nukkit.math.NukkitRandom;
import java.util.Arrays;

public class NoiseGeneratorPerlinD {
    private final NoiseGeneratorSimplexD[] b;
    private final int a;

    public NoiseGeneratorPerlinD(NukkitRandom nukkitRandom, int n) {
        this.a = n;
        this.b = new NoiseGeneratorSimplexD[n];
        for (int k = 0; k < n; ++k) {
            this.b[k] = new NoiseGeneratorSimplexD(nukkitRandom);
        }
    }

    public double getValue(double d2, double d3) {
        double d4 = 0.0;
        double d5 = 1.0;
        for (int k = 0; k < this.a; ++k) {
            d4 += this.b[k].getValue(d2 * d5, d3 * d5) / d5;
            d5 /= 2.0;
        }
        return d4;
    }

    public double[] getRegion(double[] dArray, double d2, double d3, int n, int n2, double d4, double d5, double d6) {
        return this.getRegion(dArray, d2, d3, n, n2, d4, d5, d6, 0.5);
    }

    public double[] getRegion(double[] dArray, double d2, double d3, int n, int n2, double d4, double d5, double d6, double d7) {
        if (dArray != null && dArray.length >= n * n2) {
            Arrays.fill(dArray, 0.0);
        } else {
            dArray = new double[n * n2];
        }
        double d8 = 1.0;
        double d9 = 1.0;
        for (int k = 0; k < this.a; ++k) {
            this.b[k].add(dArray, d2, d3, n, n2, d4 * d9 * d8, d5 * d9 * d8, 0.55 / d8);
            d9 *= d6;
            d8 *= d7;
        }
        return dArray;
    }
}


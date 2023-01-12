/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.vanilla.d;

import cn.nukkit.level.generator.noise.vanilla.d.NoiseGeneratorImprovedD;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitRandom;
import java.util.Arrays;

public class NoiseGeneratorOctavesD {
    private final NoiseGeneratorImprovedD[] b;
    private final int a;

    public NoiseGeneratorOctavesD(NukkitRandom nukkitRandom, int n) {
        this.a = n;
        this.b = new NoiseGeneratorImprovedD[n];
        for (int k = 0; k < n; ++k) {
            this.b[k] = new NoiseGeneratorImprovedD(nukkitRandom);
        }
    }

    public double[] generateNoiseOctaves(double[] dArray, int n, int n2, int n3, int n4, int n5, int n6, double d2, double d3, double d4) {
        if (dArray == null) {
            dArray = new double[n4 * n5 * n6];
        } else {
            Arrays.fill(dArray, 0.0);
        }
        double d5 = 1.0;
        for (int k = 0; k < this.a; ++k) {
            double d6 = (double)n * d5 * d2;
            double d7 = (double)n2 * d5 * d3;
            double d8 = (double)n3 * d5 * d4;
            long l = MathHelper.floor_double_long(d6);
            long l2 = MathHelper.floor_double_long(d8);
            d6 -= (double)l;
            d8 -= (double)l2;
            this.b[k].populateNoiseArray(dArray, d6 += (double)(l %= 0x1000000L), d7, d8 += (double)(l2 %= 0x1000000L), n4, n5, n6, d2 * d5, d3 * d5, d4 * d5, d5);
            d5 /= 2.0;
        }
        return dArray;
    }

    public double[] generateNoiseOctaves(double[] dArray, int n, int n2, int n3, int n4, double d2, double d3, double d4) {
        return this.generateNoiseOctaves(dArray, n, 10, n2, n3, 1, n4, d2, 1.0, d3);
    }
}


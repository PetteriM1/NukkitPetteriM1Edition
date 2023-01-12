/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.vanilla.f;

import cn.nukkit.level.generator.noise.vanilla.f.NoiseGeneratorSimplexF;
import cn.nukkit.math.NukkitRandom;
import java.util.Arrays;

public class NoiseGeneratorPerlinF {
    private final NoiseGeneratorSimplexF[] b;
    private final int a;

    public NoiseGeneratorPerlinF(NukkitRandom nukkitRandom, int n) {
        this.a = n;
        this.b = new NoiseGeneratorSimplexF[n];
        for (int k = 0; k < n; ++k) {
            this.b[k] = new NoiseGeneratorSimplexF(nukkitRandom);
        }
    }

    public float getValue(float f2, float f3) {
        float f4 = 0.0f;
        float f5 = 1.0f;
        for (int k = 0; k < this.a; ++k) {
            f4 += this.b[k].getValue(f2 * f5, f3 * f5) / f5;
            f5 /= 2.0f;
        }
        return f4;
    }

    public float[] getRegion(float[] fArray, float f2, float f3, int n, int n2, float f4, float f5, float f6) {
        return this.getRegion(fArray, f2, f3, n, n2, f4, f5, f6, 0.5f);
    }

    public float[] getRegion(float[] fArray, float f2, float f3, int n, int n2, float f4, float f5, float f6, float f7) {
        if (fArray != null && fArray.length >= n * n2) {
            Arrays.fill(fArray, 0.0f);
        } else {
            fArray = new float[n * n2];
        }
        float f8 = 1.0f;
        float f9 = 1.0f;
        for (int k = 0; k < this.a; ++k) {
            this.b[k].add(fArray, f2, f3, n, n2, f4 * f9 * f8, f5 * f9 * f8, 0.55f / f8);
            f9 *= f6;
            f8 *= f7;
        }
        return fArray;
    }
}


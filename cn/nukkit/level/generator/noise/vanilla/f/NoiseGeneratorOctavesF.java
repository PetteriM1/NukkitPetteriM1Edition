/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.vanilla.f;

import cn.nukkit.level.generator.noise.vanilla.f.NoiseGeneratorImprovedF;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitRandom;
import java.util.Arrays;

public class NoiseGeneratorOctavesF {
    private final NoiseGeneratorImprovedF[] b;
    private final int a;

    public NoiseGeneratorOctavesF(NukkitRandom nukkitRandom, int n) {
        this.a = n;
        this.b = new NoiseGeneratorImprovedF[n];
        for (int k = 0; k < n; ++k) {
            this.b[k] = new NoiseGeneratorImprovedF(nukkitRandom);
        }
    }

    public float[] generateNoiseOctaves(float[] fArray, int n, int n2, int n3, int n4, int n5, int n6, float f2, float f3, float f4) {
        if (fArray == null) {
            fArray = new float[n4 * n5 * n6];
        } else {
            Arrays.fill(fArray, 0.0f);
        }
        float f5 = 1.0f;
        for (int k = 0; k < this.a; ++k) {
            float f6 = (float)n * f5 * f2;
            float f7 = (float)n2 * f5 * f3;
            float f8 = (float)n3 * f5 * f4;
            int n7 = MathHelper.floor_float_int(f6);
            int n8 = MathHelper.floor_float_int(f8);
            f6 -= (float)n7;
            f8 -= (float)n8;
            this.b[k].populateNoiseArray(fArray, f6 += (float)(n7 %= 0x1000000), f7, f8 += (float)(n8 %= 0x1000000), n4, n5, n6, f2 * f5, f3 * f5, f4 * f5, f5);
            f5 = (float)((double)f5 / 2.0);
        }
        return fArray;
    }

    public float[] generateNoiseOctaves(float[] fArray, int n, int n2, int n3, int n4, float f2, float f3, float f4) {
        return this.generateNoiseOctaves(fArray, n, 10, n2, n3, 1, n4, f2, 1.0f, f3);
    }
}


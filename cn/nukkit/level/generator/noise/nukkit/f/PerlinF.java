/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.nukkit.f;

import cn.nukkit.level.generator.noise.nukkit.f.NoiseF;
import cn.nukkit.math.NukkitRandom;

public class PerlinF
extends NoiseF {
    public PerlinF(NukkitRandom nukkitRandom, float f2, float f3) {
        this(nukkitRandom, f2, f3, 1.0f);
    }

    public PerlinF(NukkitRandom nukkitRandom, float f2, float f3, float f4) {
        int n;
        this.octaves = f2;
        this.persistence = f3;
        this.expansion = f4;
        this.offsetX = nukkitRandom.nextFloat() * 256.0f;
        this.offsetY = nukkitRandom.nextFloat() * 256.0f;
        this.offsetZ = nukkitRandom.nextFloat() * 256.0f;
        this.perm = new int[512];
        for (n = 0; n < 256; ++n) {
            this.perm[n] = nukkitRandom.nextBoundedInt(256);
        }
        for (n = 0; n < 256; ++n) {
            int n2 = nukkitRandom.nextBoundedInt(256 - n) + n;
            int n3 = this.perm[n];
            this.perm[n] = this.perm[n2];
            this.perm[n2] = n3;
            this.perm[n + 256] = this.perm[n];
        }
    }

    @Override
    public float getNoise2D(float f2, float f3) {
        return this.getNoise3D(f2, f3, 0.0f);
    }

    @Override
    public float getNoise3D(float f2, float f3, float f4) {
        int n = (int)(f2 += this.offsetX);
        int n2 = (int)(f3 += this.offsetY);
        int n3 = (int)(f4 += this.offsetZ);
        int n4 = n & 0xFF;
        int n5 = n2 & 0xFF;
        int n6 = n3 & 0xFF;
        float f5 = (f2 -= (float)n) * f2 * f2 * (f2 * (f2 * 6.0f - 15.0f) + 10.0f);
        float f6 = (f3 -= (float)n2) * f3 * f3 * (f3 * (f3 * 6.0f - 15.0f) + 10.0f);
        float f7 = (f4 -= (float)n3) * f4 * f4 * (f4 * (f4 * 6.0f - 15.0f) + 10.0f);
        int n7 = this.perm[n4] + n5;
        int n8 = this.perm[n4 + 1] + n5;
        int n9 = this.perm[n7] + n6;
        int n10 = this.perm[n7 + 1] + n6;
        int n11 = this.perm[n8] + n6;
        int n12 = this.perm[n8 + 1] + n6;
        float f8 = PerlinF.grad(this.perm[n9], f2, f3, f4);
        float f9 = PerlinF.grad(this.perm[n11], f2 - 1.0f, f3, f4);
        float f10 = PerlinF.grad(this.perm[n10], f2, f3 - 1.0f, f4);
        float f11 = PerlinF.grad(this.perm[n12], f2 - 1.0f, f3 - 1.0f, f4);
        float f12 = PerlinF.grad(this.perm[n9 + 1], f2, f3, f4 - 1.0f);
        float f13 = PerlinF.grad(this.perm[n11 + 1], f2 - 1.0f, f3, f4 - 1.0f);
        float f14 = PerlinF.grad(this.perm[n10 + 1], f2, f3 - 1.0f, f4 - 1.0f);
        float f15 = PerlinF.grad(this.perm[n12 + 1], f2 - 1.0f, f3 - 1.0f, f4 - 1.0f);
        float f16 = f8 + f5 * (f9 - f8);
        float f17 = f16 + f6 * (f10 + f5 * (f11 - f10) - f16);
        float f18 = f12 + f5 * (f13 - f12);
        return f17 + f7 * (f18 + f6 * (f14 + f5 * (f15 - f14) - f18) - f17);
    }
}


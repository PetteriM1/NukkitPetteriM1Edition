/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.nukkit.d;

import cn.nukkit.level.generator.noise.nukkit.d.NoiseD;
import cn.nukkit.math.NukkitRandom;

public class PerlinD
extends NoiseD {
    public PerlinD(NukkitRandom nukkitRandom, double d2, double d3) {
        this(nukkitRandom, d2, d3, 1.0);
    }

    public PerlinD(NukkitRandom nukkitRandom, double d2, double d3, double d4) {
        int n;
        this.octaves = d2;
        this.persistence = d3;
        this.expansion = d4;
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
    public double getNoise2D(double d2, double d3) {
        return this.getNoise3D(d2, d3, 0.0);
    }

    @Override
    public double getNoise3D(double d2, double d3, double d4) {
        int n = (int)(d2 += this.offsetX);
        int n2 = (int)(d3 += this.offsetY);
        int n3 = (int)(d4 += this.offsetZ);
        int n4 = n & 0xFF;
        int n5 = n2 & 0xFF;
        int n6 = n3 & 0xFF;
        double d5 = (d2 -= (double)n) * d2 * d2 * (d2 * (d2 * 6.0 - 15.0) + 10.0);
        double d6 = (d3 -= (double)n2) * d3 * d3 * (d3 * (d3 * 6.0 - 15.0) + 10.0);
        double d7 = (d4 -= (double)n3) * d4 * d4 * (d4 * (d4 * 6.0 - 15.0) + 10.0);
        int n7 = this.perm[n4] + n5;
        int n8 = this.perm[n4 + 1] + n5;
        int n9 = this.perm[n7] + n6;
        int n10 = this.perm[n7 + 1] + n6;
        int n11 = this.perm[n8] + n6;
        int n12 = this.perm[n8 + 1] + n6;
        double d8 = PerlinD.grad(this.perm[n9], d2, d3, d4);
        double d9 = PerlinD.grad(this.perm[n11], d2 - 1.0, d3, d4);
        double d10 = PerlinD.grad(this.perm[n10], d2, d3 - 1.0, d4);
        double d11 = PerlinD.grad(this.perm[n12], d2 - 1.0, d3 - 1.0, d4);
        double d12 = PerlinD.grad(this.perm[n9 + 1], d2, d3, d4 - 1.0);
        double d13 = PerlinD.grad(this.perm[n11 + 1], d2 - 1.0, d3, d4 - 1.0);
        double d14 = PerlinD.grad(this.perm[n10 + 1], d2, d3 - 1.0, d4 - 1.0);
        double d15 = PerlinD.grad(this.perm[n12 + 1], d2 - 1.0, d3 - 1.0, d4 - 1.0);
        double d16 = d8 + d5 * (d9 - d8);
        double d17 = d16 + d6 * (d10 + d5 * (d11 - d10) - d16);
        double d18 = d12 + d5 * (d13 - d12);
        return d17 + d7 * (d18 + d6 * (d14 + d5 * (d15 - d14) - d18) - d17);
    }
}


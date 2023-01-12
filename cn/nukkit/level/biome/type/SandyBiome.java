/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.type;

import cn.nukkit.level.biome.type.CoveredBiome;

public abstract class SandyBiome
extends CoveredBiome {
    @Override
    public int getSurfaceDepth(int n, int n2, int n3) {
        return 3;
    }

    @Override
    public int getSurfaceId(int n, int n2, int n3) {
        return 192;
    }

    @Override
    public int getGroundDepth(int n, int n2, int n3) {
        return 2;
    }

    @Override
    public int getGroundId(int n, int n2, int n3) {
        return 384;
    }
}


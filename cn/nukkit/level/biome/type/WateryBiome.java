/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.type;

import cn.nukkit.level.biome.type.CoveredBiome;

public abstract class WateryBiome
extends CoveredBiome {
    @Override
    public int getSurfaceDepth(int n, int n2, int n3) {
        return 0;
    }

    @Override
    public int getSurfaceId(int n, int n2, int n3) {
        return 0;
    }

    @Override
    public int getGroundDepth(int n, int n2, int n3) {
        return 5;
    }

    @Override
    public int getGroundId(int n, int n2, int n3) {
        return 48;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.extremehills;

import cn.nukkit.level.biome.type.CoveredBiome;

public class StoneBeachBiome
extends CoveredBiome {
    public StoneBeachBiome() {
        this.setBaseHeight(0.1f);
        this.setHeightVariation(0.8f);
    }

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
        return 0;
    }

    @Override
    public int getGroundId(int n, int n2, int n3) {
        return 0;
    }

    @Override
    public String getName() {
        return "Stone Beach";
    }
}


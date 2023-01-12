/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.type;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.WaterIcePopulator;

public abstract class SnowyBiome
extends GrassyBiome {
    public SnowyBiome() {
        this.addPopulator(new WaterIcePopulator());
    }

    @Override
    public int getCoverId(int n, int n2) {
        return 1248;
    }

    @Override
    public boolean isFreezing() {
        return true;
    }

    @Override
    public boolean canRain() {
        return false;
    }
}


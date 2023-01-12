/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.beach;

import cn.nukkit.level.biome.type.SandyBiome;
import cn.nukkit.level.generator.populator.impl.WaterIcePopulator;

public class ColdBeachBiome
extends SandyBiome {
    public ColdBeachBiome() {
        this.addPopulator(new WaterIcePopulator());
        this.setBaseHeight(0.0f);
        this.setHeightVariation(0.025f);
    }

    @Override
    public int getCoverId(int n, int n2) {
        return 1248;
    }

    @Override
    public String getName() {
        return "Cold Beach";
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


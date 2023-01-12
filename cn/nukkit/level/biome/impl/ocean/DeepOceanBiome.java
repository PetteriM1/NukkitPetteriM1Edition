/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.ocean;

import cn.nukkit.level.biome.impl.ocean.OceanBiome;

public class DeepOceanBiome
extends OceanBiome {
    public DeepOceanBiome() {
        this.setBaseHeight(-1.8f);
        this.setHeightVariation(0.1f);
    }

    @Override
    public String getName() {
        return "Deep Ocean";
    }
}


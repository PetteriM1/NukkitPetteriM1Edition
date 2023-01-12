/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.ocean;

import cn.nukkit.level.biome.impl.ocean.OceanBiome;
import cn.nukkit.level.generator.populator.impl.WaterIcePopulator;

public class FrozenOceanBiome
extends OceanBiome {
    public FrozenOceanBiome() {
        WaterIcePopulator waterIcePopulator = new WaterIcePopulator();
        this.addPopulator(waterIcePopulator);
    }

    @Override
    public String getName() {
        return "Frozen Ocean";
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


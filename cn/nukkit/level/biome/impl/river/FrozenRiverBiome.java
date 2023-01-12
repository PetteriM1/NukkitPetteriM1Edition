/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.river;

import cn.nukkit.level.biome.impl.river.RiverBiome;
import cn.nukkit.level.generator.populator.impl.WaterIcePopulator;

public class FrozenRiverBiome
extends RiverBiome {
    public FrozenRiverBiome() {
        WaterIcePopulator waterIcePopulator = new WaterIcePopulator();
        this.addPopulator(waterIcePopulator);
    }

    @Override
    public String getName() {
        return "Frozen River";
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


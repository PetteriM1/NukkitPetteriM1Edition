/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.taiga;

import cn.nukkit.level.biome.impl.taiga.TaigaBiome;
import cn.nukkit.level.generator.populator.impl.WaterIcePopulator;

public class ColdTaigaBiome
extends TaigaBiome {
    public ColdTaigaBiome() {
        WaterIcePopulator waterIcePopulator = new WaterIcePopulator();
        this.addPopulator(waterIcePopulator);
        this.setBaseHeight(0.2f);
        this.setHeightVariation(0.2f);
    }

    @Override
    public String getName() {
        return "Cold Taiga";
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


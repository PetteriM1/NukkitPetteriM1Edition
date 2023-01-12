/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.extremehills;

import cn.nukkit.level.biome.impl.extremehills.ExtremeHillsMBiome;

public class ExtremeHillsPlusMBiome
extends ExtremeHillsMBiome {
    public ExtremeHillsPlusMBiome() {
        super(false);
        this.setBaseHeight(1.0f);
        this.setHeightVariation(0.5f);
    }

    @Override
    public String getName() {
        return "Extreme Hills+ M";
    }
}


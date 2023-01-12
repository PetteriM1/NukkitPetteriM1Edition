/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.extremehills;

import cn.nukkit.level.biome.impl.extremehills.ExtremeHillsBiome;

public class ExtremeHillsEdgeBiome
extends ExtremeHillsBiome {
    public ExtremeHillsEdgeBiome() {
        this.setBaseHeight(0.8f);
        this.setHeightVariation(0.3f);
    }

    @Override
    public String getName() {
        return "Extreme Hills Edge";
    }
}


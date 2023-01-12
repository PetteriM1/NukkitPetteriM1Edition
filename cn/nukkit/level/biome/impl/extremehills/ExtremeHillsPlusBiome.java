/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.extremehills;

import cn.nukkit.level.biome.impl.extremehills.ExtremeHillsBiome;

public class ExtremeHillsPlusBiome
extends ExtremeHillsBiome {
    public ExtremeHillsPlusBiome() {
        this(true);
    }

    public ExtremeHillsPlusBiome(boolean bl) {
        super(bl);
        this.setBaseHeight(1.0f);
        this.setHeightVariation(0.5f);
    }

    @Override
    public String getName() {
        return "Extreme Hills+";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.desert;

import cn.nukkit.level.biome.impl.desert.DesertBiome;

public class DesertHillsBiome
extends DesertBiome {
    public DesertHillsBiome() {
        this.setBaseHeight(0.45f);
        this.setHeightVariation(0.3f);
    }

    @Override
    public String getName() {
        return "Desert Hills";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.taiga;

import cn.nukkit.level.biome.impl.taiga.TaigaBiome;

public class TaigaHillsBiome
extends TaigaBiome {
    public TaigaHillsBiome() {
        this.setBaseHeight(0.25f);
        this.setHeightVariation(0.8f);
    }

    @Override
    public String getName() {
        return "Taiga Hills";
    }
}


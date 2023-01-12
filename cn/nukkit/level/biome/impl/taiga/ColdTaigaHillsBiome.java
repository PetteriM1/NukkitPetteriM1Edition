/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.taiga;

import cn.nukkit.level.biome.impl.taiga.ColdTaigaBiome;

public class ColdTaigaHillsBiome
extends ColdTaigaBiome {
    public ColdTaigaHillsBiome() {
        this.setBaseHeight(0.45f);
        this.setHeightVariation(0.3f);
    }

    @Override
    public String getName() {
        return "Cold Taiga Hills";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.savanna;

import cn.nukkit.level.biome.impl.savanna.SavannaPlateauBiome;

public class SavannaPlateauMBiome
extends SavannaPlateauBiome {
    public SavannaPlateauMBiome() {
        this.setBaseHeight(1.05f);
        this.setHeightVariation(1.2125001f);
    }

    @Override
    public String getName() {
        return "Savanna Plateau M";
    }

    @Override
    public boolean doesOverhang() {
        return true;
    }
}


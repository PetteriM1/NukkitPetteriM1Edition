/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.savanna;

import cn.nukkit.level.biome.impl.savanna.SavannaBiome;

public class SavannaPlateauBiome
extends SavannaBiome {
    public SavannaPlateauBiome() {
        this.setBaseHeight(1.5f);
        this.setHeightVariation(0.025f);
    }

    @Override
    public String getName() {
        return "Savanna Plateau";
    }
}


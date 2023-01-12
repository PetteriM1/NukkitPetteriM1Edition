/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.savanna;

import cn.nukkit.level.biome.impl.savanna.SavannaBiome;

public class SavannaMBiome
extends SavannaBiome {
    public SavannaMBiome() {
        this.setBaseHeight(0.3625f);
        this.setHeightVariation(1.225f);
    }

    @Override
    public String getName() {
        return "Savanna M";
    }

    @Override
    public boolean doesOverhang() {
        return true;
    }
}


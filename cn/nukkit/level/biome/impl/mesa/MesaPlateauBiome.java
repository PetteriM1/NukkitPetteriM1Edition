/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.mesa;

import cn.nukkit.level.biome.impl.mesa.MesaBiome;

public class MesaPlateauBiome
extends MesaBiome {
    public MesaPlateauBiome() {
        this.setBaseHeight(1.5f);
        this.setHeightVariation(0.025f);
        this.setMoundHeight(0);
    }

    @Override
    public String getName() {
        return "Mesa Plateau";
    }
}


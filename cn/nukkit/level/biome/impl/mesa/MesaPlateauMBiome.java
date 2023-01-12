/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.mesa;

import cn.nukkit.level.biome.impl.mesa.MesaBiome;

public class MesaPlateauMBiome
extends MesaBiome {
    public MesaPlateauMBiome() {
        this.setMoundHeight(10);
    }

    @Override
    public String getName() {
        return "Mesa Plateau M";
    }

    @Override
    protected float getMoundFrequency() {
        return 0.02f;
    }

    @Override
    protected float minHill() {
        return 0.1f;
    }
}


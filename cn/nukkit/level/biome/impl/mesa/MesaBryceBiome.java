/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.mesa;

import cn.nukkit.level.biome.impl.mesa.MesaBiome;

public class MesaBryceBiome
extends MesaBiome {
    @Override
    public String getName() {
        return "Mesa (Bryce)";
    }

    @Override
    protected float getMoundFrequency() {
        return 0.0625f;
    }

    @Override
    protected float minHill() {
        return 0.3f;
    }
}


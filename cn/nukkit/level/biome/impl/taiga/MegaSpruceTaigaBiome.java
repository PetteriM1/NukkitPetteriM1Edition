/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.taiga;

import cn.nukkit.level.biome.impl.taiga.TaigaBiome;
import cn.nukkit.level.generator.populator.impl.tree.SpruceBigTreePopulator;

public class MegaSpruceTaigaBiome
extends TaigaBiome {
    public MegaSpruceTaigaBiome() {
        SpruceBigTreePopulator spruceBigTreePopulator = new SpruceBigTreePopulator();
        spruceBigTreePopulator.setBaseAmount(6);
        this.addPopulator(spruceBigTreePopulator);
    }

    @Override
    public String getName() {
        return "Mega Spruce Taiga";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.taiga;

import cn.nukkit.level.biome.impl.taiga.TaigaBiome;
import cn.nukkit.level.generator.populator.impl.tree.SpruceBigTreePopulator;

public class MegaTaigaBiome
extends TaigaBiome {
    public MegaTaigaBiome() {
        SpruceBigTreePopulator spruceBigTreePopulator = new SpruceBigTreePopulator();
        spruceBigTreePopulator.setBaseAmount(6);
        this.addPopulator(spruceBigTreePopulator);
        this.setBaseHeight(0.2f);
        this.setHeightVariation(0.2f);
    }

    @Override
    public String getName() {
        return "Mega Taiga";
    }
}


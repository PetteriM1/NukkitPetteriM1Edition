/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.plains;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorPumpkin;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;

public class PlainsBiome
extends GrassyBiome {
    public PlainsBiome() {
        PopulatorTree populatorTree = new PopulatorTree(0);
        populatorTree.setRandomAmount(1);
        this.addPopulator(populatorTree);
        this.addPopulator(new PopulatorPumpkin());
        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
    }

    @Override
    public String getName() {
        return "Plains";
    }
}


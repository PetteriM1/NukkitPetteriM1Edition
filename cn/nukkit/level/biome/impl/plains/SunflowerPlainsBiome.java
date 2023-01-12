/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.plains;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorDoublePlant;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;

public class SunflowerPlainsBiome
extends GrassyBiome {
    public SunflowerPlainsBiome() {
        PopulatorTree populatorTree = new PopulatorTree(0);
        populatorTree.setRandomAmount(1);
        this.addPopulator(populatorTree);
        PopulatorDoublePlant populatorDoublePlant = new PopulatorDoublePlant(0);
        populatorDoublePlant.setBaseAmount(8);
        populatorDoublePlant.setRandomAmount(5);
        this.addPopulator(populatorDoublePlant);
        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
    }

    @Override
    public String getName() {
        return "Sunflower Plains";
    }
}


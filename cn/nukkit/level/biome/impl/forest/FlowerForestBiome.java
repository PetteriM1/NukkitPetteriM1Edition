/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.forest;

import cn.nukkit.level.biome.impl.forest.ForestBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorFlower;

public class FlowerForestBiome
extends ForestBiome {
    public FlowerForestBiome() {
        this(0);
    }

    public FlowerForestBiome(int n) {
        super(n);
        PopulatorFlower populatorFlower = new PopulatorFlower();
        populatorFlower.setBaseAmount(10);
        populatorFlower.addType(37, 0);
        populatorFlower.addType(38, 0);
        populatorFlower.addType(38, 2);
        populatorFlower.addType(38, 3);
        populatorFlower.addType(38, 4);
        populatorFlower.addType(38, 5);
        populatorFlower.addType(38, 6);
        populatorFlower.addType(38, 7);
        populatorFlower.addType(38, 8);
        populatorFlower.addType(38, 9);
        populatorFlower.addType(38, 10);
        populatorFlower.addType(175, 1);
        populatorFlower.addType(175, 4);
        populatorFlower.addType(175, 5);
        this.addPopulator(populatorFlower);
        this.setHeightVariation(0.4f);
    }

    @Override
    public String getName() {
        return this.type == 1 ? "Birch Forest" : "Forest";
    }
}


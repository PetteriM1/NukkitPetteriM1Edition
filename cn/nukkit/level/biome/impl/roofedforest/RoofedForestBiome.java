/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.roofedforest;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.MushroomPopulator;
import cn.nukkit.level.generator.populator.impl.PopulatorFlower;
import cn.nukkit.level.generator.populator.impl.tree.DarkOakTreePopulator;

public class RoofedForestBiome
extends GrassyBiome {
    public RoofedForestBiome() {
        DarkOakTreePopulator darkOakTreePopulator = new DarkOakTreePopulator();
        darkOakTreePopulator.setBaseAmount(20);
        darkOakTreePopulator.setRandomAmount(10);
        this.addPopulator(darkOakTreePopulator);
        PopulatorFlower populatorFlower = new PopulatorFlower();
        populatorFlower.setBaseAmount(3);
        populatorFlower.addType(37, 0);
        populatorFlower.addType(38, 0);
        MushroomPopulator mushroomPopulator = new MushroomPopulator();
        mushroomPopulator.setBaseAmount(1);
        mushroomPopulator.setRandomAmount(2);
        this.addPopulator(mushroomPopulator);
    }

    @Override
    public String getName() {
        return "Roofed Forest";
    }
}


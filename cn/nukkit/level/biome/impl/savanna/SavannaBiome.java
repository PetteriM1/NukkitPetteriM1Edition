/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.savanna;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorFlower;
import cn.nukkit.level.generator.populator.impl.tree.SavannaTreePopulator;

public class SavannaBiome
extends GrassyBiome {
    public SavannaBiome() {
        SavannaTreePopulator savannaTreePopulator = new SavannaTreePopulator(4);
        savannaTreePopulator.setBaseAmount(1);
        this.addPopulator(savannaTreePopulator);
        PopulatorFlower populatorFlower = new PopulatorFlower();
        populatorFlower.setBaseAmount(2);
        this.addPopulator(populatorFlower);
        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
    }

    @Override
    public String getName() {
        return "Savanna";
    }

    @Override
    public boolean canRain() {
        return false;
    }
}


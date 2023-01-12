/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.desert;

import cn.nukkit.level.biome.type.SandyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorCactus;
import cn.nukkit.level.generator.populator.impl.PopulatorDeadBush;
import cn.nukkit.level.generator.populator.impl.PopulatorWell;

public class DesertBiome
extends SandyBiome {
    public DesertBiome() {
        PopulatorCactus populatorCactus = new PopulatorCactus();
        populatorCactus.setBaseAmount(2);
        this.addPopulator(populatorCactus);
        PopulatorDeadBush populatorDeadBush = new PopulatorDeadBush();
        populatorDeadBush.setBaseAmount(2);
        this.addPopulator(populatorDeadBush);
        this.addPopulator(new PopulatorWell());
        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
    }

    @Override
    public String getName() {
        return "Desert";
    }

    @Override
    public boolean canRain() {
        return false;
    }
}


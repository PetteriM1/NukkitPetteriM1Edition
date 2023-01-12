/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.beach;

import cn.nukkit.level.biome.type.SandyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorSugarcane;
import cn.nukkit.level.generator.populator.impl.PopulatorTallSugarcane;
import cn.nukkit.level.generator.populator.impl.PopulatorWell;

public class BeachBiome
extends SandyBiome {
    public BeachBiome() {
        PopulatorSugarcane populatorSugarcane = new PopulatorSugarcane();
        populatorSugarcane.setRandomAmount(3);
        this.addPopulator(populatorSugarcane);
        PopulatorTallSugarcane populatorTallSugarcane = new PopulatorTallSugarcane();
        populatorTallSugarcane.setRandomAmount(1);
        this.addPopulator(populatorTallSugarcane);
        this.addPopulator(new PopulatorWell());
        this.setBaseHeight(0.0f);
        this.setHeightVariation(0.025f);
    }

    @Override
    public String getName() {
        return "Beach";
    }
}


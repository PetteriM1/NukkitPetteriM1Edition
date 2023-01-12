/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.taiga;

import cn.nukkit.level.biome.impl.taiga.ColdTaigaBiome;
import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorDoublePlant;
import cn.nukkit.level.generator.populator.impl.PopulatorSweetBerryBush;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;

public class TaigaBiome
extends GrassyBiome {
    public TaigaBiome() {
        PopulatorTree populatorTree = new PopulatorTree(1);
        populatorTree.setBaseAmount(10);
        this.addPopulator(populatorTree);
        if (!(this instanceof ColdTaigaBiome)) {
            PopulatorDoublePlant populatorDoublePlant = new PopulatorDoublePlant(3);
            populatorDoublePlant.setBaseAmount(2);
            this.addPopulator(populatorDoublePlant);
            PopulatorSweetBerryBush populatorSweetBerryBush = new PopulatorSweetBerryBush();
            populatorSweetBerryBush.setRandomAmount(3);
            this.addPopulator(populatorSweetBerryBush);
        }
        this.setBaseHeight(0.2f);
        this.setHeightVariation(0.2f);
    }

    @Override
    public String getName() {
        return "Taiga";
    }
}


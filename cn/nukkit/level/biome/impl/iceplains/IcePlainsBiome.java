/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.iceplains;

import cn.nukkit.level.biome.type.SnowyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;

public class IcePlainsBiome
extends SnowyBiome {
    public IcePlainsBiome() {
        PopulatorTree populatorTree = new PopulatorTree(1);
        populatorTree.setRandomAmount(1);
        this.addPopulator(populatorTree);
        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
    }

    @Override
    public String getName() {
        return "Ice Plains";
    }
}


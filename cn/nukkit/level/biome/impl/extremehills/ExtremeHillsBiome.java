/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.extremehills;

import cn.nukkit.block.Block;
import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;

public class ExtremeHillsBiome
extends GrassyBiome {
    public ExtremeHillsBiome() {
        this(true);
    }

    public ExtremeHillsBiome(boolean bl) {
        if (bl) {
            PopulatorTree populatorTree = new PopulatorTree(1);
            populatorTree.setBaseAmount(2);
            populatorTree.setRandomAmount(2);
            this.addPopulator(populatorTree);
        }
        this.addPopulator(new PopulatorOre(1, new OreType[]{new OreType(Block.get(129), 11, 1, 0, 32), new OreType(Block.get(97), 7, 9, 0, 63)}));
        this.setBaseHeight(1.0f);
        this.setHeightVariation(0.5f);
    }

    @Override
    public String getName() {
        return "Extreme Hills";
    }

    @Override
    public boolean doesOverhang() {
        return true;
    }
}


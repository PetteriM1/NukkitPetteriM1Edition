/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.mesa;

import cn.nukkit.level.biome.impl.mesa.MesaPlateauBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;

public class MesaPlateauFBiome
extends MesaPlateauBiome {
    public MesaPlateauFBiome() {
        PopulatorTree populatorTree = new PopulatorTree(0);
        populatorTree.setBaseAmount(2);
        populatorTree.setRandomAmount(1);
        this.addPopulator(populatorTree);
    }

    @Override
    public int getCoverId(int n, int n2) {
        return 32;
    }

    @Override
    public String getName() {
        return "Mesa Plateau F";
    }
}


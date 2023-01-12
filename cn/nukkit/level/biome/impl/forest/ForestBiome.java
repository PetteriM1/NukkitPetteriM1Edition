/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.forest;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorFallenTree;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;

public class ForestBiome
extends GrassyBiome {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_BIRCH = 1;
    public static final int TYPE_BIRCH_TALL = 2;
    public final int type;

    public ForestBiome() {
        this(0);
    }

    public ForestBiome(int n) {
        this.type = n;
        PopulatorTree populatorTree = new PopulatorTree(n == 2 ? 10 : 2);
        populatorTree.setBaseAmount(n == 0 ? 3 : 6);
        this.addPopulator(populatorTree);
        if (n == 0) {
            populatorTree = new PopulatorTree(0);
            populatorTree.setBaseAmount(3);
            this.addPopulator(populatorTree);
        }
        PopulatorFallenTree populatorFallenTree = new PopulatorFallenTree();
        populatorFallenTree.setType(n);
        this.addPopulator(populatorFallenTree);
    }

    @Override
    public String getName() {
        switch (this.type) {
            case 1: {
                return "Birch Forest";
            }
            case 2: {
                return "Birch Forest M";
            }
        }
        return "Forest";
    }
}


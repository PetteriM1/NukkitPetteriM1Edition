/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.forest;

import cn.nukkit.level.biome.impl.forest.ForestBiome;

public class ForestHillsBiome
extends ForestBiome {
    public ForestHillsBiome() {
        this(0);
    }

    public ForestHillsBiome(int n) {
        super(n);
        this.setBaseHeight(0.45f);
        this.setHeightVariation(0.3f);
    }

    @Override
    public String getName() {
        switch (this.type) {
            case 1: {
                return "Birch Forest Hills";
            }
            case 2: {
                return "Birch Forest Hills M";
            }
        }
        return "Forest Hills";
    }
}


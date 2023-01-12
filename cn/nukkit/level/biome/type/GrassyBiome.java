/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.type;

import cn.nukkit.level.biome.type.CoveredBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorDoublePlant;
import cn.nukkit.level.generator.populator.impl.PopulatorGrass;

public abstract class GrassyBiome
extends CoveredBiome {
    public GrassyBiome() {
        PopulatorGrass populatorGrass = new PopulatorGrass();
        populatorGrass.setBaseAmount(30);
        this.addPopulator(populatorGrass);
        PopulatorDoublePlant populatorDoublePlant = new PopulatorDoublePlant(2);
        populatorDoublePlant.setBaseAmount(5);
        this.addPopulator(populatorDoublePlant);
    }

    @Override
    public int getSurfaceId(int n, int n2, int n3) {
        return 32;
    }

    @Override
    public int getGroundId(int n, int n2, int n3) {
        return 48;
    }
}


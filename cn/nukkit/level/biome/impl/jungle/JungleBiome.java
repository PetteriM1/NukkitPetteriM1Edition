/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorBamboo;
import cn.nukkit.level.generator.populator.impl.PopulatorMelon;
import cn.nukkit.level.generator.populator.impl.tree.JungleBigTreePopulator;
import cn.nukkit.level.generator.populator.impl.tree.JungleTreePopulator;

public class JungleBiome
extends GrassyBiome {
    public JungleBiome() {
        JungleTreePopulator jungleTreePopulator = new JungleTreePopulator();
        jungleTreePopulator.setBaseAmount(10);
        this.addPopulator(jungleTreePopulator);
        JungleBigTreePopulator jungleBigTreePopulator = new JungleBigTreePopulator();
        jungleBigTreePopulator.setBaseAmount(7);
        this.addPopulator(jungleBigTreePopulator);
        PopulatorMelon populatorMelon = new PopulatorMelon();
        populatorMelon.setRandomAmount(2);
        this.addPopulator(populatorMelon);
        PopulatorBamboo populatorBamboo = new PopulatorBamboo();
        populatorBamboo.setRandomAmount(2);
        this.addPopulator(populatorBamboo);
    }

    @Override
    public String getName() {
        return "Jungle";
    }
}


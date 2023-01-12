/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.level.biome.impl.jungle.JungleBiome;
import cn.nukkit.level.generator.populator.impl.tree.JungleFloorPopulator;

public class JungleMBiome
extends JungleBiome {
    public JungleMBiome() {
        JungleFloorPopulator jungleFloorPopulator = new JungleFloorPopulator();
        jungleFloorPopulator.setBaseAmount(10);
        jungleFloorPopulator.setRandomAmount(5);
        this.addPopulator(jungleFloorPopulator);
        this.setBaseHeight(0.2f);
        this.setHeightVariation(0.4f);
    }

    @Override
    public String getName() {
        return "Jungle M";
    }
}


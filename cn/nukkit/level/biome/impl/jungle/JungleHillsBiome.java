/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.level.biome.impl.jungle.JungleBiome;

public class JungleHillsBiome
extends JungleBiome {
    public JungleHillsBiome() {
        this.setBaseHeight(0.45f);
        this.setHeightVariation(0.3f);
    }

    @Override
    public String getName() {
        return "Jungle Hills";
    }
}


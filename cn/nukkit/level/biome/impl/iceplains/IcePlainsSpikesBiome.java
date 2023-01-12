/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.iceplains;

import cn.nukkit.level.biome.impl.iceplains.IcePlainsBiome;
import cn.nukkit.level.biome.impl.iceplains.b;

public class IcePlainsSpikesBiome
extends IcePlainsBiome {
    public IcePlainsSpikesBiome() {
        b b2 = new b(null);
        this.addPopulator(b2);
    }

    @Override
    public int getSurfaceId(int n, int n2, int n3) {
        return 1280;
    }

    @Override
    public String getName() {
        return "Ice Plains Spikes";
    }
}


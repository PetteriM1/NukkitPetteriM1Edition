/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.extremehills;

import cn.nukkit.level.biome.impl.extremehills.ExtremeHillsPlusBiome;
import cn.nukkit.level.generator.noise.nukkit.f.SimplexF;
import cn.nukkit.math.NukkitRandom;

public class ExtremeHillsMBiome
extends ExtremeHillsPlusBiome {
    private static final SimplexF f = new SimplexF(new NukkitRandom(), 1.0f, 0.25f, 0.015625f);

    public ExtremeHillsMBiome() {
        this(true);
    }

    public ExtremeHillsMBiome(boolean bl) {
        super(bl);
        this.setBaseHeight(1.0f);
        this.setHeightVariation(0.5f);
    }

    @Override
    public String getName() {
        return "Extreme Hills M";
    }

    @Override
    public int getSurfaceId(int n, int n2, int n3) {
        return f.noise2D(n, n3, true) < -0.75f ? 208 : super.getSurfaceId(n, n2, n3);
    }

    @Override
    public int getSurfaceDepth(int n, int n2, int n3) {
        return f.noise2D(n, n3, true) < -0.75f ? 4 : super.getSurfaceDepth(n, n2, n3);
    }

    @Override
    public int getGroundDepth(int n, int n2, int n3) {
        return f.noise2D(n, n3, true) < -0.75f ? 0 : super.getGroundDepth(n, n2, n3);
    }

    @Override
    public boolean doesOverhang() {
        return false;
    }
}


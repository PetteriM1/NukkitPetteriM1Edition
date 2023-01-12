/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.mesa;

import cn.nukkit.level.biome.type.CoveredBiome;
import cn.nukkit.level.generator.noise.nukkit.f.SimplexF;
import cn.nukkit.level.generator.populator.impl.PopulatorCactus;
import cn.nukkit.level.generator.populator.impl.PopulatorDeadBush;
import cn.nukkit.math.NukkitRandom;
import java.util.Arrays;
import java.util.SplittableRandom;

public class MesaBiome
extends CoveredBiome {
    static final int[] h = new int[64];
    static final SimplexF f = new SimplexF(new NukkitRandom(937478913L), 2.0f, 0.25f, 0.25f);
    static final SimplexF i = new SimplexF(new NukkitRandom(193759875L), 2.0f, 0.25f, 0.03125f);
    protected int moundHeight;
    private final SimplexF g = new SimplexF(new NukkitRandom(347228794L), 2.0f, 0.25f, this.getMoundFrequency());

    public MesaBiome() {
        PopulatorCactus populatorCactus = new PopulatorCactus();
        populatorCactus.setBaseAmount(1);
        populatorCactus.setRandomAmount(1);
        this.addPopulator(populatorCactus);
        PopulatorDeadBush populatorDeadBush = new PopulatorDeadBush();
        populatorDeadBush.setBaseAmount(3);
        populatorDeadBush.setRandomAmount(2);
        this.addPopulator(populatorDeadBush);
        this.setMoundHeight(17);
    }

    private static void a(SplittableRandom splittableRandom, int n, int n2) {
        for (int k = 0; k < splittableRandom.nextInt(4) + n; ++k) {
            int n3 = splittableRandom.nextInt(h.length);
            for (int i2 = 0; i2 < splittableRandom.nextInt(2) + 1 && n3 < h.length; ++i2) {
                MesaBiome.h[n3++] = n2;
            }
        }
    }

    public void setMoundHeight(int n) {
        this.moundHeight = n;
    }

    @Override
    public int getSurfaceDepth(int n, int n2, int n3) {
        return n2 < 71 + Math.round((f.noise2D(n, n3, true) + 1.0f) * 1.5f) ? 3 : n2 - 66;
    }

    @Override
    public int getSurfaceId(int n, int n2, int n3) {
        if (n2 < 71 + Math.round((f.noise2D(n, n3, true) + 1.0f) * 1.5f)) {
            return 193;
        }
        int n4 = h[n2 + Math.round((i.noise2D(n, n3, true) + 1.0f) * 1.5f) & 0x3F];
        return (n4 == -1 ? 2752 : 2544) | Math.max(0, n4);
    }

    @Override
    public int getGroundDepth(int n, int n2, int n3) {
        return n2 < 71 + Math.round((f.noise2D(n, n3, true) + 1.0f) * 1.5f) ? 2 : 0;
    }

    @Override
    public int getGroundId(int n, int n2, int n3) {
        return 2864;
    }

    @Override
    public String getName() {
        return "Mesa";
    }

    protected float getMoundFrequency() {
        return 0.0078125f;
    }

    @Override
    public int getHeightOffset(int n, int n2) {
        float f2;
        float f3 = this.g.noise2D(n, n2, true);
        return f3 > (f2 = this.minHill()) && f3 < f2 + 0.2f ? (int)((f3 - f2) * 5.0f * (float)this.moundHeight) : (f3 < f2 + 0.1f ? 0 : this.moundHeight);
    }

    protected float minHill() {
        return -0.1f;
    }

    @Override
    public boolean canRain() {
        return false;
    }

    static {
        SplittableRandom splittableRandom = new SplittableRandom(29864L);
        Arrays.fill(h, -1);
        MesaBiome.a(splittableRandom, 14, 1);
        MesaBiome.a(splittableRandom, 8, 4);
        MesaBiome.a(splittableRandom, 7, 12);
        MesaBiome.a(splittableRandom, 10, 14);
        int n = 0;
        for (int k = 0; k < splittableRandom.nextInt(3) + 3 && (n += splittableRandom.nextInt(6) + 4) < h.length - 3; ++k) {
            if (splittableRandom.nextInt(2) == 0 || n < h.length - 1 && splittableRandom.nextInt(2) == 0) {
                MesaBiome.h[n - 1] = 8;
                continue;
            }
            MesaBiome.h[n] = 0;
        }
    }
}


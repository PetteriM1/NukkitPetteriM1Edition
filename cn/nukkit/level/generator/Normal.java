/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.BiomeSelector;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.noise.vanilla.f.NoiseGeneratorOctavesF;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorBedrock;
import cn.nukkit.level.generator.populator.impl.PopulatorCaves;
import cn.nukkit.level.generator.populator.impl.PopulatorDungeon;
import cn.nukkit.level.generator.populator.impl.PopulatorGroundCover;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.impl.PopulatorSpring;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

public class Normal
extends Generator {
    private static final float[] c = new float[25];
    private List<Populator> o = Collections.emptyList();
    private List<Populator> d = Collections.emptyList();
    public static final int seaHeight = 64;
    public NoiseGeneratorOctavesF scaleNoise;
    public NoiseGeneratorOctavesF depthNoise;
    private ChunkManager k;
    private NukkitRandom e;
    private long l;
    private long r;
    private BiomeSelector n;
    private final ThreadLocal<float[]> m = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<float[]> p = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<float[]> i = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<float[]> h = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<float[]> f = ThreadLocal.withInitial(() -> new float[825]);
    private NoiseGeneratorOctavesF j;
    private NoiseGeneratorOctavesF q;
    private NoiseGeneratorOctavesF g;

    public Normal() {
    }

    public Normal(Map<String, Object> map) {
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.k;
    }

    @Override
    public String getName() {
        return "normal";
    }

    @Override
    public Map<String, Object> getSettings() {
        return Collections.emptyMap();
    }

    public Biome pickBiome(int n, int n2) {
        return this.n.pickBiome(n, n2);
    }

    @Override
    public void init(ChunkManager chunkManager, NukkitRandom nukkitRandom) {
        this.k = chunkManager;
        this.e = nukkitRandom;
        SplittableRandom splittableRandom = new SplittableRandom();
        this.e.setSeed(this.k.getSeed());
        this.l = splittableRandom.nextLong();
        this.r = splittableRandom.nextLong();
        this.e.setSeed(this.k.getSeed());
        this.n = new BiomeSelector(this.e);
        this.j = new NoiseGeneratorOctavesF(nukkitRandom, 16);
        this.q = new NoiseGeneratorOctavesF(nukkitRandom, 16);
        this.g = new NoiseGeneratorOctavesF(nukkitRandom, 8);
        this.scaleNoise = new NoiseGeneratorOctavesF(nukkitRandom, 10);
        this.depthNoise = new NoiseGeneratorOctavesF(nukkitRandom, 16);
        this.d = ImmutableList.of(new PopulatorBedrock(), new PopulatorGroundCover());
        this.o = ImmutableList.of(new PopulatorOre(1, new OreType[]{new OreType(Block.get(16), 20, 17, 0, 128), new OreType(Block.get(15), 20, 9, 0, 64), new OreType(Block.get(73), 8, 8, 0, 16), new OreType(Block.get(21), 1, 7, 0, 30), new OreType(Block.get(14), 2, 9, 0, 32), new OreType(Block.get(56), 1, 8, 0, 16), new OreType(Block.get(3), 10, 33, 0, 128), new OreType(Block.get(13), 8, 33, 0, 128), new OreType(Block.get(1, 1), 10, 33, 0, 80), new OreType(Block.get(1, 3), 10, 33, 0, 80), new OreType(Block.get(1, 5), 10, 33, 0, 80)}), new PopulatorCaves(), new PopulatorDungeon(), new PopulatorSpring(8, ImmutableList.of(1), 15, 8, 255), new PopulatorSpring(10, ImmutableList.of(1), 10, 16, 255));
    }

    @Override
    public void generateChunk(int n, int n2) {
        int n3;
        int n4;
        int n5 = n << 4;
        int n6 = n2 << 4;
        this.e.setSeed((long)n * this.l ^ (long)n2 * this.r ^ this.k.getSeed());
        BaseFullChunk baseFullChunk = this.k.getChunk(n, n2);
        float[] fArray = this.depthNoise.generateNoiseOctaves(this.m.get(), n << 2, n2 << 2, 5, 5, 200.0f, 200.0f, 0.5f);
        this.m.set(fArray);
        float[] fArray2 = this.g.generateNoiseOctaves(this.p.get(), n << 2, 0, n2 << 2, 5, 33, 5, 11.406866f, 4.277575f, 11.406866f);
        this.p.set(fArray2);
        float[] fArray3 = this.j.generateNoiseOctaves(this.i.get(), n << 2, 0, n2 << 2, 5, 33, 5, 684.412f, 684.412f, 684.412f);
        this.i.set(fArray3);
        float[] fArray4 = this.q.generateNoiseOctaves(this.h.get(), n << 2, 0, n2 << 2, 5, 33, 5, 684.412f, 684.412f, 684.412f);
        this.h.set(fArray4);
        float[] fArray5 = this.f.get();
        int n7 = 0;
        int n8 = 0;
        for (n4 = 0; n4 < 5; ++n4) {
            for (n3 = 0; n3 < 5; ++n3) {
                float f2;
                float f3;
                float f4 = 0.0f;
                float f5 = 0.0f;
                float f6 = 0.0f;
                Biome biome = this.pickBiome(n5 + (n4 << 2), n6 + (n3 << 2));
                for (int k = -2; k <= 2; ++k) {
                    for (int i2 = -2; i2 <= 2; ++i2) {
                        Biome biome2 = this.pickBiome(n5 + (n4 << 2) + k, n6 + (n3 << 2) + i2);
                        f3 = biome2.getBaseHeight();
                        float f7 = biome2.getHeightVariation();
                        f2 = c[k + 2 + (i2 + 2) * 5] / (f3 + 2.0f);
                        if (biome2.getBaseHeight() > biome.getBaseHeight()) {
                            f2 /= 2.0f;
                        }
                        f4 += f7 * f2;
                        f5 += f3 * f2;
                        f6 += f2;
                    }
                }
                f4 /= f6;
                f5 /= f6;
                f4 = f4 * 0.9f + 0.1f;
                f5 = (f5 * 4.0f - 1.0f) / 8.0f;
                float f8 = fArray[n8] / 8000.0f;
                if (f8 < 0.0f) {
                    f8 = -f8 * 0.3f;
                }
                if ((f8 = f8 * 3.0f - 2.0f) < 0.0f) {
                    if ((f8 /= 2.0f) < -1.0f) {
                        f8 = -1.0f;
                    }
                    f8 /= 1.4f;
                    f8 /= 2.0f;
                } else {
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                    f8 /= 8.0f;
                }
                ++n8;
                float f9 = f5;
                float f10 = f4;
                f9 += f8 * 0.2f;
                f9 = f9 * 8.5f / 8.0f;
                f3 = 8.5f + f9 * 4.0f;
                for (int k = 0; k < 33; ++k) {
                    f2 = ((float)k - f3) * 12.0f * 128.0f / 256.0f / f10;
                    if (f2 < 0.0f) {
                        f2 *= 4.0f;
                    }
                    float f11 = fArray3[n7] / 512.0f;
                    float f12 = fArray4[n7] / 512.0f;
                    float f13 = (fArray2[n7] / 10.0f + 1.0f) / 2.0f;
                    float f14 = MathHelper.denormalizeClamp(f11, f12, f13) - f2;
                    if (k > 29) {
                        float f15 = (float)(k - 29) / 3.0f;
                        f14 = f14 * (1.0f - f15) + -10.0f * f15;
                    }
                    fArray5[n7] = f14;
                    ++n7;
                }
            }
        }
        for (n4 = 0; n4 < 4; ++n4) {
            n3 = n4 * 5;
            int n9 = (n4 + 1) * 5;
            for (int k = 0; k < 4; ++k) {
                int n10 = (n3 + k) * 33;
                int n11 = (n3 + k + 1) * 33;
                int n12 = (n9 + k) * 33;
                int n13 = (n9 + k + 1) * 33;
                for (int i3 = 0; i3 < 32; ++i3) {
                    double d2 = fArray5[n10 + i3];
                    double d3 = fArray5[n11 + i3];
                    double d4 = fArray5[n12 + i3];
                    double d5 = fArray5[n13 + i3];
                    double d6 = ((double)fArray5[n10 + i3 + 1] - d2) * 0.125;
                    double d7 = ((double)fArray5[n11 + i3 + 1] - d3) * 0.125;
                    double d8 = ((double)fArray5[n12 + i3 + 1] - d4) * 0.125;
                    double d9 = ((double)fArray5[n13 + i3 + 1] - d5) * 0.125;
                    for (int i4 = 0; i4 < 8; ++i4) {
                        double d10 = d2;
                        double d11 = d3;
                        double d12 = (d4 - d2) * 0.25;
                        double d13 = (d5 - d3) * 0.25;
                        for (int i5 = 0; i5 < 4; ++i5) {
                            double d14 = (d11 - d10) * 0.25;
                            double d15 = d10 - d14;
                            for (int i6 = 0; i6 < 4; ++i6) {
                                double d16;
                                d15 += d14;
                                if (d16 > 0.0) {
                                    baseFullChunk.setBlockId((n4 << 2) + i5, (i3 << 3) + i4, (k << 2) + i6, 1);
                                    continue;
                                }
                                if ((i3 << 3) + i4 > 64) continue;
                                baseFullChunk.setBlockId((n4 << 2) + i5, (i3 << 3) + i4, (k << 2) + i6, 9);
                            }
                            d10 += d12;
                            d11 += d13;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                    }
                }
            }
        }
        for (n4 = 0; n4 < 16; ++n4) {
            for (n3 = 0; n3 < 16; ++n3) {
                baseFullChunk.setBiome(n4, n3, this.n.pickBiome(n5 | n4, n6 | n3));
            }
        }
        for (Populator populator : this.d) {
            populator.populate(this.k, n, n2, this.e, baseFullChunk);
        }
    }

    @Override
    public void populateChunk(int n, int n2) {
        this.e.setSeed((long)(0xDEADBEEF ^ n << 8 ^ n2) ^ this.k.getSeed());
        for (Populator populator : this.o) {
            populator.populate(this.k, n, n2, this.e, this.k.getChunk(n, n2));
        }
        Biome biome = EnumBiome.getBiome(this.k.getChunk(n, n2).getBiomeId(7, 7));
        biome.populateChunk(this.k, n, n2, this.e);
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0.5, 256.0, 0.5);
    }

    static {
        for (int k = -2; k <= 2; ++k) {
            for (int i2 = -2; i2 <= 2; ++i2) {
                Normal.c[k + 2 + (i2 + 2) * 5] = (float)(10.0 / Math.sqrt((float)(k * k + i2 * i2) + 0.2f));
            }
        }
    }
}


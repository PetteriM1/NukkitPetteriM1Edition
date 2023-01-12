/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.biome.type.CoveredBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitRandom;
import java.util.Random;

public class PopulatorCaves
extends Populator {
    protected int checkAreaSize = 8;
    private Random a;
    public static int caveRarity = 7;
    public static int caveFrequency = 40;
    public static int caveMinAltitude = 8;
    public static int caveMaxAltitude = 67;
    public static int individualCaveRarity = 25;
    public static int caveSystemFrequency = 1;
    public static int caveSystemPocketChance = 0;
    public static int caveSystemPocketMinSize = 0;
    public static int caveSystemPocketMaxSize = 4;
    public static boolean evenCaveDistribution = false;
    public int worldHeightCap = 128;

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        this.a = new Random();
        this.a.setSeed(chunkManager.getSeed());
        long l = this.a.nextLong();
        long l2 = this.a.nextLong();
        int n3 = this.checkAreaSize;
        for (int k = n - n3; k <= n + n3; ++k) {
            for (int i2 = n2 - n3; i2 <= n2 + n3; ++i2) {
                long l3 = (long)k * l;
                long l4 = (long)i2 * l2;
                this.a.setSeed(l3 ^ l4 ^ chunkManager.getSeed());
                this.generateChunk(k, i2, fullChunk);
            }
        }
    }

    protected void generateLargeCaveNode(long l, FullChunk fullChunk, double d2, double d3, double d4) {
        this.generateCaveNode(l, fullChunk, d2, d3, d4, 1.0f + this.a.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5);
    }

    protected void generateCaveNode(long l, FullChunk fullChunk, double d2, double d3, double d4, float f2, float f3, float f4, int n, int n2, double d5) {
        boolean bl;
        int n3;
        int n4 = fullChunk.getX();
        int n5 = fullChunk.getZ();
        double d6 = (n4 << 4) + 8;
        double d7 = (n5 << 4) + 8;
        float f5 = 0.0f;
        float f6 = 0.0f;
        Random random = new Random(l);
        if (n2 <= 0) {
            n3 = (this.checkAreaSize << 4) - 16;
            n2 = n3 - random.nextInt(n3 >> 2);
        }
        n3 = 0;
        if (n == -1) {
            n = n2 >> 1;
            n3 = 1;
        }
        int n6 = random.nextInt(n2 >> 1) + (n2 >> 2);
        boolean bl2 = bl = random.nextInt(6) == 0;
        while (n < n2) {
            double d8 = 1.5 + (double)(MathHelper.sin((float)n * 3.141593f / (float)n2) * f2 * 1.0f);
            double d9 = d8 * d5;
            float f7 = MathHelper.cos(f4);
            float f8 = MathHelper.sin(f4);
            d2 += (double)(MathHelper.cos(f3) * f7);
            d3 += (double)f8;
            d4 += (double)(MathHelper.sin(f3) * f7);
            f4 = bl ? (f4 *= 0.92f) : (f4 *= 0.7f);
            f4 += f6 * 0.1f;
            f3 += f5 * 0.1f;
            f6 *= 0.9f;
            f5 *= 0.75f;
            f6 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f5 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (n3 == 0 && n == n6 && f2 > 1.0f && n2 > 0) {
                this.generateCaveNode(random.nextLong(), fullChunk, d2, d3, d4, random.nextFloat() * 0.5f + 0.5f, f3 - 1.570796f, f4 / 3.0f, n, n2, 1.0);
                this.generateCaveNode(random.nextLong(), fullChunk, d2, d3, d4, random.nextFloat() * 0.5f + 0.5f, f3 + 1.570796f, f4 / 3.0f, n, n2, 1.0);
                return;
            }
            if (n3 != 0 || random.nextInt(4) != 0) {
                double d10 = d2 - d6;
                double d11 = d4 - d7;
                double d12 = n2 - n;
                double d13 = f2 + 2.0f + 16.0f;
                if (d10 * d10 + d11 * d11 - d12 * d12 > d13 * d13) {
                    return;
                }
                if (!(d2 < d6 - 16.0 - d8 * 2.0 || d4 < d7 - 16.0 - d8 * 2.0 || d2 > d6 + 16.0 + d8 * 2.0 || d4 > d7 + 16.0 + d8 * 2.0)) {
                    int n7;
                    int n8;
                    int n9 = MathHelper.floor(d2 - d8) - (n4 << 4) - 1;
                    int n10 = MathHelper.floor(d2 + d8) - (n4 << 4) + 1;
                    int n11 = MathHelper.floor(d3 - d9) - 1;
                    int n12 = MathHelper.floor(d3 + d9) + 1;
                    int n13 = MathHelper.floor(d4 - d8) - (n5 << 4) - 1;
                    int n14 = MathHelper.floor(d4 + d8) - (n5 << 4) + 1;
                    if (n9 < 0) {
                        n9 = 0;
                    }
                    if (n10 > 16) {
                        n10 = 16;
                    }
                    if (n11 < 1) {
                        n11 = 1;
                    }
                    if (n12 > this.worldHeightCap - 8) {
                        n12 = this.worldHeightCap - 8;
                    }
                    if (n13 < 0) {
                        n13 = 0;
                    }
                    if (n14 > 16) {
                        n14 = 16;
                    }
                    boolean bl3 = false;
                    for (n8 = n9; !bl3 && n8 < n10; ++n8) {
                        for (int k = n13; !bl3 && k < n14; ++k) {
                            for (int i2 = n12 + 1; !bl3 && i2 >= n11 - 1; --i2) {
                                if (i2 < 0 || i2 >= this.worldHeightCap) continue;
                                n7 = fullChunk.getBlockId(n8, i2, k);
                                if (n7 == 8 || n7 == 9) {
                                    bl3 = true;
                                }
                                if (i2 == n11 - 1 || n8 == n9 || n8 == n10 - 1 || k == n13 || k == n14 - 1) continue;
                                i2 = n11;
                            }
                        }
                    }
                    if (!bl3) {
                        for (n8 = n9; n8 < n10; ++n8) {
                            double d14 = ((double)(n8 + (n4 << 4)) + 0.5 - d2) / d8;
                            for (n7 = n13; n7 < n14; ++n7) {
                                double d15 = ((double)(n7 + (n5 << 4)) + 0.5 - d4) / d8;
                                boolean bl4 = false;
                                if (!(d14 * d14 + d15 * d15 < 1.0)) continue;
                                for (int k = n12; k > n11; --k) {
                                    Biome biome;
                                    double d16 = ((double)(k - 1) + 0.5 - d3) / d9;
                                    if (!(d16 > -0.7) || !(d14 * d14 + d16 * d16 + d15 * d15 < 1.0) || !((biome = EnumBiome.getBiome(fullChunk.getBiomeId(n8, n7))) instanceof CoveredBiome)) continue;
                                    int n15 = fullChunk.getBlockId(n8, k, n7);
                                    if (n15 == 2 || n15 == 110) {
                                        bl4 = true;
                                    }
                                    if (k - 1 < 10) {
                                        fullChunk.setBlock(n8, k, n7, 10);
                                        continue;
                                    }
                                    fullChunk.setBlock(n8, k, n7, 0);
                                    if (!bl4 || fullChunk.getBlockId(n8, k - 1, n7) != 3) continue;
                                    fullChunk.setFullBlockId(n8, k - 1, n7, ((CoveredBiome)biome).getSurfaceId(n8, k - 1, n7));
                                }
                            }
                        }
                        if (n3 != 0) break;
                    }
                }
            }
            ++n;
        }
    }

    protected void generateChunk(int n, int n2, FullChunk fullChunk) {
        int n3 = this.a.nextInt(this.a.nextInt(this.a.nextInt(caveFrequency) + 1) + 1);
        if (evenCaveDistribution) {
            n3 = caveFrequency;
        }
        if (this.a.nextInt(100) >= caveRarity) {
            n3 = 0;
        }
        for (int k = 0; k < n3; ++k) {
            double d2 = (n << 4) + this.a.nextInt(16);
            double d3 = evenCaveDistribution ? (double)PopulatorCaves.numberInRange(this.a, caveMinAltitude, caveMaxAltitude) : (double)(this.a.nextInt(this.a.nextInt(caveMaxAltitude - caveMinAltitude + 1) + 1) + caveMinAltitude);
            double d4 = (n2 << 4) + this.a.nextInt(16);
            int n4 = caveSystemFrequency;
            boolean bl = false;
            if (this.a.nextInt(100) <= individualCaveRarity) {
                this.generateLargeCaveNode(this.a.nextLong(), fullChunk, d2, d3, d4);
                bl = true;
            }
            if (bl || this.a.nextInt(100) <= caveSystemPocketChance - 1) {
                n4 += PopulatorCaves.numberInRange(this.a, caveSystemPocketMinSize, caveSystemPocketMaxSize);
            }
            while (n4 > 0) {
                --n4;
                float f2 = this.a.nextFloat() * 6.283186f;
                float f3 = (this.a.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float f4 = this.a.nextFloat() * 2.0f + this.a.nextFloat();
                this.generateCaveNode(this.a.nextLong(), fullChunk, d2, d3, d4, f4, f2, f3, 0, 0, 1.0);
            }
        }
    }

    public static int numberInRange(Random random, int n, int n2) {
        return n + random.nextInt(n2 - n + 1);
    }
}


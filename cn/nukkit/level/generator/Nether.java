/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.noise.nukkit.f.SimplexF;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorGlowStone;
import cn.nukkit.level.generator.populator.impl.PopulatorGroundFire;
import cn.nukkit.level.generator.populator.impl.PopulatorLava;
import cn.nukkit.level.generator.populator.impl.PopulatorNetherWart;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Nether
extends Generator {
    private ChunkManager c;
    private NukkitRandom h;
    private static final int e = 32;
    private final SimplexF[] g = new SimplexF[3];
    private final List<Populator> d = new ArrayList<Populator>();
    private long f;
    private long i;

    public Nether() {
    }

    public Nether(Map<String, Object> map) {
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public String getName() {
        return "nether";
    }

    @Override
    public Map<String, Object> getSettings() {
        return Collections.emptyMap();
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.c;
    }

    @Override
    public void init(ChunkManager chunkManager, NukkitRandom nukkitRandom) {
        this.c = chunkManager;
        this.h = nukkitRandom;
        this.h.setSeed(this.c.getSeed());
        for (int k = 0; k < this.g.length; ++k) {
            this.g[k] = new SimplexF(this.h, 4.0f, 0.25f, 0.015625f);
        }
        this.h.setSeed(this.c.getSeed());
        this.f = ThreadLocalRandom.current().nextLong();
        this.i = ThreadLocalRandom.current().nextLong();
        PopulatorOre populatorOre = new PopulatorOre(87, new OreType[]{new OreType(Block.get(153), 16, 24, 10, 117, 87), new OreType(Block.get(88), 12, 23, 0, 105, 87), new OreType(Block.get(13), 2, 64, 5, 105, 87), new OreType(Block.get(213), 4, 64, 26, 33, 87), new OreType(Block.get(10), 1, 16, 0, 32, 87)});
        this.d.add(populatorOre);
        PopulatorGroundFire populatorGroundFire = new PopulatorGroundFire();
        populatorGroundFire.setRandomAmount(3);
        this.d.add(populatorGroundFire);
        PopulatorLava populatorLava = new PopulatorLava();
        populatorLava.setRandomAmount(2);
        this.d.add(populatorLava);
        this.d.add(new PopulatorGlowStone());
        PopulatorNetherWart populatorNetherWart = new PopulatorNetherWart();
        populatorNetherWart.setBaseAmount(5);
        populatorNetherWart.setRandomAmount(8);
        this.d.add(populatorNetherWart);
    }

    @Override
    public void generateChunk(int n, int n2) {
        int n3 = n << 4;
        int n4 = n2 << 4;
        this.h.setSeed((long)n * this.f ^ (long)n2 * this.i ^ this.c.getSeed());
        BaseFullChunk baseFullChunk = this.c.getChunk(n, n2);
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                int n5;
                baseFullChunk.setBiomeId(k, i2, EnumBiome.HELL.biome.getId());
                baseFullChunk.setBlockId(k, 0, i2, 7);
                for (n5 = 115; n5 < 127; ++n5) {
                    baseFullChunk.setBlockId(k, n5, i2, 87);
                }
                baseFullChunk.setBlockId(k, 127, i2, 7);
                for (n5 = 1; n5 < 127; ++n5) {
                    if (this.getNoise(n3 | k, n5, n4 | i2) > 0.0f) {
                        baseFullChunk.setBlockId(k, n5, i2, 87);
                        continue;
                    }
                    if (n5 > 32) continue;
                    baseFullChunk.setBlockId(k, n5, i2, 11);
                    baseFullChunk.setBlockLight(k, n5 + 1, i2, 15);
                }
            }
        }
    }

    @Override
    public void populateChunk(int n, int n2) {
        BaseFullChunk baseFullChunk = this.c.getChunk(n, n2);
        this.h.setSeed((long)(0xDEADBEEF ^ n << 8 ^ n2) ^ this.c.getSeed());
        for (Populator populator : this.d) {
            populator.populate(this.c, n, n2, this.h, baseFullChunk);
        }
        Biome biome = EnumBiome.getBiome(EnumBiome.HELL.id);
        biome.populateChunk(this.c, n, n2, this.h);
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0.5, 64.0, 0.5);
    }

    public float getNoise(int n, int n2, int n3) {
        float f2 = 0.0f;
        for (int k = 0; k < this.g.length; ++k) {
            f2 += this.g[k].noise3D(n >> k, n2, n3 >> k, true);
        }
        return f2;
    }
}


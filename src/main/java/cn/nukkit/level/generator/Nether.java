package cn.nukkit.level.generator;

import cn.nukkit.block.*;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.noise.nukkit.f.SimplexF;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorNetherWart;
import cn.nukkit.level.generator.populator.impl.PopulatorGlowStone;
import cn.nukkit.level.generator.populator.impl.PopulatorGroundFire;
import cn.nukkit.level.generator.populator.impl.PopulatorLava;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Nether extends Generator {

    private ChunkManager level;
    private NukkitRandom nukkitRandom;
    private double lavaHeight = 32;
    private SimplexF[] noiseGen = new SimplexF[3];
    private final List<Populator> populators = new ArrayList<>();
    private List<Populator> generationPopulators = new ArrayList<>();

    private long localSeed1;
    private long localSeed2;

    public Nether() {
        this(new HashMap<>());
    }

    public Nether(Map<String, Object> options) {
    }

    @Override
    public int getId() {
        return Generator.TYPE_NETHER;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_NETHER;
    }

    @Override
    public String getName() {
        return "nether";
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.nukkitRandom.setSeed(this.level.getSeed());

        for (int i = 0; i < noiseGen.length; i++)   {
            noiseGen[i] = new SimplexF(nukkitRandom, 4, 0.25f, 0.015625f);
        }

        this.nukkitRandom.setSeed(this.level.getSeed());
        this.localSeed1 = ThreadLocalRandom.current().nextLong();
        this.localSeed2 = ThreadLocalRandom.current().nextLong();

        PopulatorOre ores = new PopulatorOre(Block.NETHERRACK, new OreType[]{
                new OreType(Block.get(BlockID.QUARTZ_ORE), 20, 16, 0, 128),
                new OreType(Block.get(BlockID.SOUL_SAND), 5, 64, 0, 128),
                new OreType(Block.get(BlockID.GRAVEL), 5, 64, 0, 128),
                new OreType(Block.get(BlockID.MAGMA), 5, 12, 30, 33),
                new OreType(Block.get(BlockID.LAVA), 1, 16, 0, (int) this.lavaHeight),
        });
        this.populators.add(ores);

        PopulatorGroundFire groundFire = new PopulatorGroundFire();
        groundFire.setBaseAmount(1);
        groundFire.setRandomAmount(2);
        this.populators.add(groundFire);

        PopulatorLava lava = new PopulatorLava();
        lava.setBaseAmount(1);
        lava.setRandomAmount(2);
        this.populators.add(lava);
        this.populators.add(new PopulatorGlowStone());

        PopulatorNetherWart netherWart = new PopulatorNetherWart();
        netherWart.setBaseAmount(5);
        netherWart.setRandomAmount(8);
        this.populators.add(netherWart);
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        this.nukkitRandom.setSeed(chunkX * localSeed1 ^ chunkZ * localSeed2 ^ this.level.getSeed());

        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                chunk.setBiomeId(x, z, EnumBiome.HELL.biome.getId());

                chunk.setBlockId(x, 0, z, Block.BEDROCK);
                for (int y = 115; y < 127; ++y) {
                    chunk.setBlockId(x, y, z, Block.NETHERRACK);
                }
                chunk.setBlockId(x, 127, z, Block.BEDROCK);
                for (int y = 1; y < 127; ++y) {
                    if (getNoise(baseX | x, y, baseZ | z) > 0) {
                        chunk.setBlockId(x, y, z, Block.NETHERRACK);
                    } else if (y <= this.lavaHeight) {
                        chunk.setBlockId(x, y, z, Block.STILL_LAVA);
                        chunk.setBlockLight(x, y + 1, z, 15);
                    }
                }
            }
        }
        for (Populator populator : this.generationPopulators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
        this.nukkitRandom.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
        for (Populator populator : this.populators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }

        Biome biome = EnumBiome.getBiome(chunk.getBiomeId(7, 7));
        biome.populateChunk(this.level, chunkX, chunkZ, this.nukkitRandom);
    }

    public Vector3 getSpawn() {
        return new Vector3(0.5, 64, 0.5);
    }

    public float getNoise(int x, int y, int z)  {
        float val = 0f;
        for (int i = 0; i < noiseGen.length; i++)   {
            val += noiseGen[i].noise3D(x >> i, y, z >> i, true);
        }
        return val;
    }
}
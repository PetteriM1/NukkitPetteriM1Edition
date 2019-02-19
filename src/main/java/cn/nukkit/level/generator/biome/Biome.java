package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

import java.util.ArrayList;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class Biome extends cn.nukkit.level.biome.Biome{

    public static final int OCEAN = 0;
    public static final int PLAINS = 1;
    public static final int DESERT = 2;
    public static final int MOUNTAINS = 3;
    public static final int FOREST = 4;
    public static final int TAIGA = 5;
    public static final int SWAMP = 6;
    public static final int RIVER = 7;
    public static final int HELL = 8;
    public static final int ICE_PLAINS = 12;
    public static final int MUSHROOM_ISLAND = 14;
    public static final int BEACH = 16;
    public static final int SMALL_MOUNTAINS = 20;
    public static final int JUNGLE = 21;
    public static final int BIRCH_FOREST = 27;
    public static final int ROOFED_FOREST = 29;
    public static final int TUNDRA = 30;
    public static final int SAVANNA = 35;
    public static final int FLOWER_FOREST = 132;

    private static final Biome[] biomes = new Biome[256];

    private final ArrayList<Populator> populators = new ArrayList<>();

    private int minElevation;
    private int maxElevation;

    private Block[] groundCover;

    protected double rainfall = 0.5;
    protected double temperature = 0.5;
    protected int grassColor = 0;

    protected static void register(int id, Biome biome) {
        biome.setId(id);
        biomes[id] = biome;
    }

    public static void init() {
        register(OCEAN, new OceanBiome());
        register(PLAINS, new PlainBiome());
        register(DESERT, new DesertBiome());
        register(MOUNTAINS, new MountainsBiome());
        register(FOREST, new ForestBiome());
        register(TAIGA, new TaigaBiome());
        register(SWAMP, new SwampBiome());
        register(RIVER, new RiverBiome());
        register(ICE_PLAINS, new IcePlainsBiome());
        register(SMALL_MOUNTAINS, new SmallMountainsBiome());
        register(BIRCH_FOREST, new ForestBiome(ForestBiome.TYPE_BIRCH));
        register(JUNGLE, new JungleBiome());
        register(ROOFED_FOREST, new RoofedForestBiome());
        register(MUSHROOM_ISLAND, new MushroomIsland());
        register(SAVANNA, new SavannaBiome());
        register(BEACH, new BeachBiome());
        register(TUNDRA, new TundraBiome());
        register(FLOWER_FOREST, new FlowerForestBiome());
        register(HELL, new HellBiome());
    }

    public static Biome getBiome(int id) {
        Biome biome = biomes[id];
        return biome != null ? biome : biomes[OCEAN];
    }

    /**
     * Get Biome by name.
     *
     * @param name Name of biome. Name could contain symbol "_" instead of space
     * @return Biome. Null - when biome was not found
     */
    public static Biome getBiome(String name) {
        for (Biome biome : biomes) {
            if (biome != null) {
                if (biome.getName().equalsIgnoreCase(name.replace("_", " "))) return biome;
            }
        }
        return null;
    }

    public void clearPopulators() {
        this.populators.clear();
    }

    public void addPopulator(Populator populator) {
        this.populators.add(populator);
    }

    public void populateChunk(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        for (Populator populator : populators) {
            populator.populate(level, chunkX, chunkZ, random, level.getChunk(chunkX, chunkZ));
        }
    }

    public int getMinElevation() {
        return minElevation;
    }

    public int getMaxElevation() {
        return maxElevation;
    }

    public void setElevation(int min, int max) {
        this.minElevation = min;
        this.maxElevation = max;
    }

    public Block[] getGroundCover() {
        return groundCover;
    }

    public void setGroundCover(Block[] covers) {
        this.groundCover = covers;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getRainfall() {
        return rainfall;
    }

    abstract public int getColor();
}

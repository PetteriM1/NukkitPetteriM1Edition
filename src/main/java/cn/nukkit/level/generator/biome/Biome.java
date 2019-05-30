package cn.nukkit.level.generator.biome;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

import java.util.ArrayList;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@SuppressWarnings("unused") /* KEEP THIS CLASS FOR OLD API SUPPORT */
public abstract class Biome extends cn.nukkit.level.biome.Biome {

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
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        biome.setId(id);
        biomes[id] = biome;
    }

    public static Biome getBiome(int id) {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        Biome biome = biomes[id];
        return biome != null ? biome : biomes[OCEAN];
    }

    public static Biome getBiome(String name) {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        for (Biome biome : biomes) {
            if (biome != null) {
                if (biome.getName().equalsIgnoreCase(name.replace("_", " "))) return biome;
            }
        }
        return null;
    }

    public void clearPopulators() {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        this.populators.clear();
    }

    public void addPopulator(Populator populator) {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        this.populators.add(populator);
    }

    public void populateChunk(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        for (Populator populator : populators) {
            populator.populate(level, chunkX, chunkZ, random, level.getChunk(chunkX, chunkZ));
        }
    }

    public int getMinElevation() {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        return minElevation;
    }

    public int getMaxElevation() {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        return maxElevation;
    }

    public void setElevation(int min, int max) {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        this.minElevation = min;
        this.maxElevation = max;
    }

    public Block[] getGroundCover() {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        return groundCover;
    }

    public void setGroundCover(Block[] covers) {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        this.groundCover = covers;
    }

    public double getTemperature() {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        return temperature;
    }

    public double getRainfall() {
        Server.getInstance().getLogger().warning("A plugin is using the old biome class cn.nukkit.level.generator.biome.Biome");
        return rainfall;
    }

    abstract public int getColor();
}

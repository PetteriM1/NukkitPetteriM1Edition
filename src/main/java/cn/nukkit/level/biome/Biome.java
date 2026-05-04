package cn.nukkit.level.biome;

import cn.nukkit.block.BlockID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class Biome implements BlockID {

    public static final Biome[] biomes = new Biome[256];
    public static final List<Biome> unorderedBiomes = new ObjectArrayList<>();
    private static final QuickLookupTable runtimeId2Identifier = new QuickLookupTable();

    private final ArrayList<Populator> populators = new ArrayList<>();
    private int id;
    private float baseHeight = 0.1f;
    private float heightVariation = 0.3f;

    static {
        JsonObject json = Utils.loadJsonResource("biome_id_map.json").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            int biomeId = entry.getValue().getAsInt();
            runtimeId2Identifier.put(biomeId, entry.getKey());
        }
    }

    private static class QuickLookupTable {

        private String[] array = new String[256];

        private String get(int k) {
            if (k < 0 || array == null || k >= array.length) {
                return null;
            }

            return array[k];
        }

        private void put(int k, String v) {
            if (k < 0) {
                throw new IllegalArgumentException();
            }

            if (array.length <= k) {
                array = Arrays.copyOf(array, k + 1);
            }

            array[k] = v;
        }
    }

    public void setBaseHeight(float baseHeight) {
        this.baseHeight = baseHeight;
    }

    public void setHeightVariation(float heightVariation) {
        this.heightVariation = heightVariation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getBaseHeight() {
        return baseHeight;
    }

    public float getHeightVariation() {
        return heightVariation;
    }

    public int getId() {
        return id;
    }

    public abstract String getName();

    public ArrayList<Populator> getPopulators() {
        return populators;
    }

    /**
     * Whether or not water should freeze into ice on generation
     *
     * @return overhang
     */
    public boolean isFreezing() {
        return false;
    }

    public void addPopulator(Populator populator) {
        this.populators.add(populator);
    }

    public boolean canRain() {
        return true;
    }

    public void clearPopulators() {
        this.populators.clear();
    }

    /**
     * Whether or not overhangs should generate in this biome (places where solid blocks generate over air)
     * <p>
     * This should probably be used with a custom max elevation or things can look stupid
     *
     * @return overhang
     */
    public boolean doesOverhang() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

    public static Biome getBiome(int id) {
        Biome biome = biomes[id];
        return biome != null ? biome : EnumBiome.OCEAN.biome;
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

    public static int getBiomeIdOrCorrect(int biomeId) {
        if (runtimeId2Identifier.get(biomeId) == null) {
            return EnumBiome.OCEAN.id;
        }
        return biomeId;
    }

    public static String getBiomeNameFromId(int biomeId) {
        return runtimeId2Identifier.get(biomeId);
    }

    /**
     * How much offset should be added to the min/max heights at this position
     *
     * @param x x
     * @param z z
     * @return height offset
     */
    public int getHeightOffset(int x, int z) {
        return 0;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void populateChunk(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        FullChunk chunk = level.getChunk(chunkX, chunkZ);
        for (Populator populator : populators) {
            populator.populate(level, chunkX, chunkZ, random, chunk);
        }
    }

    protected static void register(int id, Biome biome) {
        biome.setId(id);
        biomes[id] = biome;
        unorderedBiomes.add(biome);
    }
}

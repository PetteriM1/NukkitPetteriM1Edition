/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome;

import cn.nukkit.block.BlockID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Biome
implements BlockID {
    public static final Biome[] biomes = new Biome[256];
    public static final List<Biome> unorderedBiomes = new ObjectArrayList<Biome>();
    private static final Int2ObjectMap<String> b = new Int2ObjectOpenHashMap<String>();
    private final ArrayList<Populator> e = new ArrayList();
    private int d;
    private float c = 0.1f;
    private float a = 0.3f;

    public static String getBiomeNameFromId(int n) {
        return (String)b.get(n);
    }

    public static int getBiomeIdOrCorrect(int n) {
        if (b.get(n) == null) {
            return EnumBiome.OCEAN.id;
        }
        return n;
    }

    protected static void register(int n, Biome biome) {
        biome.setId(n);
        Biome.biomes[n] = biome;
        unorderedBiomes.add(biome);
    }

    public static Biome getBiome(int n) {
        Biome biome = biomes[n];
        return biome != null ? biome : EnumBiome.OCEAN.biome;
    }

    public static Biome getBiome(String string) {
        for (Biome biome : biomes) {
            if (biome == null || !biome.getName().equalsIgnoreCase(string.replace("_", " "))) continue;
            return biome;
        }
        return null;
    }

    public void clearPopulators() {
        this.e.clear();
    }

    public void addPopulator(Populator populator) {
        this.e.add(populator);
    }

    public void populateChunk(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom) {
        BaseFullChunk baseFullChunk = chunkManager.getChunk(n, n2);
        for (Populator populator : this.e) {
            populator.populate(chunkManager, n, n2, nukkitRandom, baseFullChunk);
        }
    }

    public ArrayList<Populator> getPopulators() {
        return this.e;
    }

    public int getId() {
        return this.d;
    }

    public void setId(int n) {
        this.d = n;
    }

    public abstract String getName();

    public void setBaseHeight(float f2) {
        this.c = f2;
    }

    public void setHeightVariation(float f2) {
        this.a = f2;
    }

    public float getBaseHeight() {
        return this.c;
    }

    public float getHeightVariation() {
        return this.a;
    }

    public int hashCode() {
        return this.d;
    }

    public boolean equals(Object object) {
        return this.hashCode() == object.hashCode();
    }

    public boolean isFreezing() {
        return false;
    }

    public boolean doesOverhang() {
        return false;
    }

    public int getHeightOffset(int n, int n2) {
        return 0;
    }

    public boolean canRain() {
        return true;
    }

    static {
        try (InputStream inputStream = Biome.class.getClassLoader().getResourceAsStream("biome_id_map.json");){
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            for (String string : jsonObject.keySet()) {
                int n = jsonObject.get(string).getAsInt();
                b.put(n, string);
            }
        }
        catch (IOException | NullPointerException exception) {
            throw new AssertionError("Unable to load biome mapping from biome_id_map.json", exception);
        }
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}


package cn.nukkit.level.generator.biome;

import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.math.NukkitRandom;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BiomeSelector {
    private final Biome fallback;
    private final Simplex temperature;
    private final Simplex rainfall;

    private final boolean[] biomes = new boolean[256];

    private int[] map = new int[64 * 64];

    public BiomeSelector(NukkitRandom random, Biome fallback) {
        this.fallback = fallback;
        this.temperature = new Simplex(random, 2F, 1F / 8F, 1F / 1024F);
        this.rainfall = new Simplex(random, 2F, 1F / 8F, 1F / 1024F);
    }

    public int lookup(double temperature, double rainfall) {
        if (rainfall < 0.10) {
            return Biome.MUSHROOM_ISLAND;
        } else if (rainfall < 0.18) {
            return Biome.SWAMP;
        } else if (rainfall < 0.25) {
            return Biome.PLAINS;
        } else if (rainfall < 0.55) {
            if (temperature < 0.30) {
                return Biome.TAIGA;
            } else if (temperature < 0.75) {
                return Biome.DESERT;
            } else {
                return Biome.SAVANNA;
            }
        } else if (rainfall < 0.60) {
            return Biome.FLOWER_FOREST;
        } else if (rainfall < 0.65) {
            return Biome.ROOFED_FOREST;
        } else if (rainfall < 0.70) {
            return Biome.BIRCH_FOREST;
        } else if (rainfall < 0.75) {
            return Biome.PLAINS;
        } else if (rainfall < 0.82) {
            if (temperature < 0.20) {
                return Biome.ICE_PLAINS;
            } else if (temperature < 0.30) {
                return Biome.TUNDRA;
            } else {
                return Biome.FOREST;
            }
        } else if (rainfall < 1.0) {
            return Biome.JUNGLE;
        }
        return Biome.PLAINS;
    }

    public void recalculate() {
        this.map = new int[64 * 64];
        for (int i = 0; i < 64; ++i) {
            for (int j = 0; j < 64; ++j) {
                this.map[i + (j << 6)] = this.lookup(i / 63d, j / 63d);
            }
        }
    }

    public void addBiome(Biome biome) {
        this.biomes[biome.getId()] = true;
    }

    public double getTemperature(double x, double z) {
        return (this.temperature.noise2D(x, z, true) + 1) / 2;
    }

    public double getRainfall(double x, double z) {
        return (this.rainfall.noise2D(x, z, true) + 1) / 2;
    }

    public Biome pickBiome(double x, double z) {
        int temperature = (int) (this.getTemperature(x, z) * 63);
        int rainfall = (int) (this.getRainfall(x, z) * 63);

        int biomeId = this.map[temperature + (rainfall << 6)];
        if (this.biomes[biomeId]) {
            return Biome.getBiome(biomeId);
        } else {
            return this.fallback;
        }
    }
}

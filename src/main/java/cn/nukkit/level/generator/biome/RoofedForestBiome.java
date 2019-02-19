package cn.nukkit.level.generator.biome;

public class RoofedForestBiome extends GrassyBiome {

    public RoofedForestBiome() {
        super();
        this.setElevation(62, 68);
        this.temperature = 0.7;
        this.rainfall = 0.8;
    }

    @Override
    public String getName() {
        return "Roofed Forest";
    }
}

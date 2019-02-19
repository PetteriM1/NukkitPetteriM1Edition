package cn.nukkit.level.generator.biome;

public class JungleBiome extends GrassyBiome {

    public JungleBiome() {
        super();
        this.setElevation(62, 63);
        this.temperature = 1.2;
        this.rainfall = 0.9;
    }

    @Override
    public String getName() {
        return "Jungle";
    }
}

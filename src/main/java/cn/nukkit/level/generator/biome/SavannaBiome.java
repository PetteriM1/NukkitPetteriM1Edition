package cn.nukkit.level.generator.biome;

public class SavannaBiome extends GrassyBiome {

    public SavannaBiome() {
        super();
        this.setElevation(62, 68);
        this.temperature = 1.2;
        this.rainfall = 0;
    }

    @Override
    public String getName() {
        return "Savanna";
    }
}

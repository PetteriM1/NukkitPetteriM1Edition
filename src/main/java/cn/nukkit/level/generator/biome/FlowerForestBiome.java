package cn.nukkit.level.generator.biome;

/**
 * Created by PetteriM1
 */
public class FlowerForestBiome extends GrassyBiome {

    public FlowerForestBiome() {
        super();
        this.setElevation(63, 78);
        this.temperature = 0.7;
        this.rainfall = 0.8;
    }

    @Override
    public String getName() {
        return "Flower Forest";
    }
}

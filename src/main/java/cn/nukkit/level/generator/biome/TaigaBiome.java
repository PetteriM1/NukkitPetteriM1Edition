package cn.nukkit.level.generator.biome;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class TaigaBiome extends GrassyBiome {

    public TaigaBiome() {
        super();
        this.setElevation(63, 81);
        this.temperature = 0.25;
        this.rainfall = 0.8;
    }

    @Override
    public String getName() {
        return "Taiga";
    }
}

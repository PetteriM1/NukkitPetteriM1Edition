package cn.nukkit.level.generator.biome;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class PlainBiome extends GrassyBiome {

    public PlainBiome() {
        super();
        this.setElevation(63, 74);
        this.temperature = 0.8;
        this.rainfall = 0.4;
    }

    @Override
    public String getName() {
        return "Plains";
    }
}

package cn.nukkit.level.generator.biome;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class MountainsBiome extends GrassyBiome {

    public MountainsBiome() {
        super();
        this.setElevation(63, 127);
        this.temperature = 0.4;
        this.rainfall = 0.5;
    }

    @Override
    public String getName() {
        return "Mountains";
    }
}

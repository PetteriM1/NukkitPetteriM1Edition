package cn.nukkit.level.generator.biome;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class ForestBiome extends GrassyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_BIRCH = 1;

    public final int type;

    public ForestBiome() {
        this(TYPE_NORMAL);
    }

    public ForestBiome(int type) {
        super();

        this.type = type;

        this.setElevation(63, 81);

        if (type == TYPE_BIRCH) {
            this.temperature = 0.5;
            this.rainfall = 0.5;
        } else {
            this.temperature = 0.7;
            this.rainfall = 0.8;
        }
    }

    @Override
    public String getName() {
        return this.type == TYPE_BIRCH ? "Birch Forest" : "Forest";
    }
}

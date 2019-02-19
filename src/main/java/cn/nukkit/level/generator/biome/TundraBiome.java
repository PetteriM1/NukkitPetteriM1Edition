package cn.nukkit.level.generator.biome;

/**
 * Created by PetteriM1
 */
public class TundraBiome extends SnowyBiome {

    public TundraBiome() {
        super();
        this.setElevation(63, 74);
        this.temperature = 0.25;
        this.rainfall = 0.8;
    }

    @Override
    public String getName() {
        return "Tundra";
    }
}

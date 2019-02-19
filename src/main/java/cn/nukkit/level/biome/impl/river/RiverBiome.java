package cn.nukkit.level.biome.impl.river;

import cn.nukkit.level.biome.type.WateryBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorSugarcane;
import cn.nukkit.level.generator.populator.impl.PopulatorTallSugarcane;

/**
 * @author DaPorkchop_
 * Nukkit Project
 */
public class RiverBiome extends WateryBiome {

    public RiverBiome() {
        PopulatorSugarcane sugarcane = new PopulatorSugarcane();
        sugarcane.setBaseAmount(2);
        sugarcane.setRandomAmount(2);
        this.addPopulator(sugarcane);
        PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
        tallSugarcane.setRandomAmount(1);

        this.setBaseHeight(-0.5f);
        this.setHeightVariation(0f);
    }

    @Override
    public String getName() {
        return "River";
    }
}

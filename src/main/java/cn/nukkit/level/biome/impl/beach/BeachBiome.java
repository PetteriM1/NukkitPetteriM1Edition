package cn.nukkit.level.biome.impl.beach;

import cn.nukkit.level.biome.type.SandyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorSugarcane;
import cn.nukkit.level.generator.populator.impl.PopulatorTallSugarcane;

/**
 * @author PeratX
 * Nukkit Project
 */
public class BeachBiome extends SandyBiome {
    public BeachBiome() {
        PopulatorSugarcane sugarcane = new PopulatorSugarcane();
        sugarcane.setBaseAmount(2);
        sugarcane.setRandomAmount(2);
        this.addPopulator(sugarcane);
        PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
        tallSugarcane.setRandomAmount(1);

        this.setBaseHeight(0f);
        this.setHeightVariation(0.025f);
    }

    @Override
    public String getName() {
        return "Beach";
    }
}

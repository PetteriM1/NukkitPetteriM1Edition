package cn.nukkit.level.biome.impl.beach;

import cn.nukkit.level.biome.type.SandyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorCactus;
import cn.nukkit.level.generator.populator.impl.PopulatorSugarcane;
import cn.nukkit.level.generator.populator.impl.PopulatorTallSugarcane;
import cn.nukkit.level.generator.populator.impl.PopulatorWell;

/**
 * @author PeratX
 * Nukkit Project
 */
public class BeachBiome extends SandyBiome {
    public BeachBiome() {
        PopulatorSugarcane sugarcane = new PopulatorSugarcane();
        //sugarcane.setBaseAmount(2);
        sugarcane.setRandomAmount(2);
        this.addPopulator(sugarcane);
        PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
        //tallSugarcane.setBaseAmount(1);
        tallSugarcane.setRandomAmount(1);
        this.addPopulator(tallSugarcane);
        PopulatorCactus cactus = new PopulatorCactus();
        cactus.setRandomAmount(1);
        this.addPopulator(cactus);
        this.addPopulator(new PopulatorWell());

        this.setBaseHeight(0f);
        this.setHeightVariation(0.025f);
    }

    @Override
    public String getName() {
        return "Beach";
    }
}

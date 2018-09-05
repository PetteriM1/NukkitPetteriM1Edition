package cn.nukkit.level.generator.biome;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorTree;

/**
 * Created by PetteriM1
 */
public class TundraBiome extends SnowyBiome {

    public TundraBiome() {
        super();

        PopulatorGrass grass = new PopulatorGrass();
        grass.setRandomAmount(2);
        this.addPopulator(grass);

        PopulatorTree trees = new PopulatorTree(BlockSapling.SPRUCE);
        trees.setRandomAmount(1);
        this.addPopulator(trees);

        this.setElevation(63, 74);

        this.temperature = 0.25;
        this.rainfall = 0.8;
    }

    @Override
    public String getName() {
        return "Tundra";
    }
}

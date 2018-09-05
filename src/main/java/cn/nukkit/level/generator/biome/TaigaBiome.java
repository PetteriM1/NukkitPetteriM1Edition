package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.populator.PopulatorFlower;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.PopulatorTree;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class TaigaBiome extends GrassyBiome {

    public TaigaBiome() {
        super();

        PopulatorFlower fern = new PopulatorFlower();
        fern.setBaseAmount(8);
        fern.addType(Block.TALL_GRASS, 2);
        this.addPopulator(fern);

        PopulatorTree trees = new PopulatorTree(BlockSapling.SPRUCE);
        trees.setBaseAmount(10);
        this.addPopulator(trees);

        PopulatorTallGrass tallGrass = new PopulatorTallGrass();
        tallGrass.setBaseAmount(1);

        this.addPopulator(tallGrass);

        this.setElevation(63, 81);

        this.temperature = 0.25;
        this.rainfall = 0.8;
    }

    @Override
    public String getName() {
        return "Taiga";
    }
}

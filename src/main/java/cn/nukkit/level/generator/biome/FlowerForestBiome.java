package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.level.generator.populator.PopulatorFlower;
import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorTree;

/**
 * Created by PetteriM1
 */
public class FlowerForestBiome extends GrassyBiome {

    public FlowerForestBiome() {
        super();

        PopulatorTree trees1 = new PopulatorTree(BlockSapling.BIRCH);
        PopulatorTree trees2 = new PopulatorTree(BlockSapling.OAK);
        trees1.setBaseAmount(3);
        trees2.setBaseAmount(3);
        this.addPopulator(trees1);
        this.addPopulator(trees2);

        PopulatorGrass grass = new PopulatorGrass();
        grass.setBaseAmount(10);
        this.addPopulator(grass);

        PopulatorFlower flower = new PopulatorFlower();
        flower.setBaseAmount(25);
        flower.addType(Block.DANDELION, 0);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_POPPY);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_ALLIUM);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_AZURE_BLUET);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_RED_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_ORANGE_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_WHITE_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_PINK_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_OXEYE_DAISY);
        this.addPopulator(flower);

        this.setElevation(63, 78);

        this.temperature = 0.7;
        this.rainfall = 0.8;
    }

    @Override
    public String getName() {
        return "Flower Forest";
    }
}

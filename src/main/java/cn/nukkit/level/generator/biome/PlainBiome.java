package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.block.BlockWater;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.*;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class PlainBiome extends GrassyBiome {

    public PlainBiome() {
        super();
        PopulatorLake lakes = new PopulatorLake();
        lakes.setOreTypes(new OreType[]{
                new OreType(new BlockWater(), 1, 30, 62, 64)
        });
        PopulatorSugarcane sugarcane = new PopulatorSugarcane();
        sugarcane.setBaseAmount(6);
        PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
        tallSugarcane.setBaseAmount(60);
        PopulatorGrass grass = new PopulatorGrass();
        grass.setBaseAmount(40);
        PopulatorTallGrass tallGrass = new PopulatorTallGrass();
        tallGrass.setBaseAmount(7);
        PopulatorSunflower sunflower = new PopulatorSunflower();
        sunflower.setBaseAmount(6);
        PopulatorPumpkin pumpkins = new PopulatorPumpkin();
        pumpkins.setBaseAmount(1);
        PopulatorFlower flower = new PopulatorFlower();
        flower.setBaseAmount(10);
        flower.addType(Block.DANDELION, 0);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_POPPY);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_AZURE_BLUET);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_RED_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_ORANGE_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_WHITE_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_PINK_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_OXEYE_DAISY);

        this.addPopulator(lakes);
        this.addPopulator(sugarcane);
        this.addPopulator(tallSugarcane);
        this.addPopulator(grass);
        this.addPopulator(tallGrass);
        this.addPopulator(sunflower);
        this.addPopulator(pumpkins);
        this.addPopulator(flower);

        this.setElevation(63, 74);

        this.temperature = 0.8;
        this.rainfall = 0.4;
    }

    @Override
    public String getName() {
        return "Plains";
    }
}

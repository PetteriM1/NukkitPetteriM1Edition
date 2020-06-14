package cn.nukkit.level.biome.impl.river;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.biome.type.WateryBiome;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
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
        this.addPopulator(tallSugarcane);

        PopulatorOre ores = new PopulatorOre(DIRT, new OreType[]{
                new OreType(Block.get(BlockID.GRAVEL), 13, 33, 50, 60),
                new OreType(Block.get(BlockID.CLAY_BLOCK), 8, 22, 50, 60)
        });

        this.addPopulator(ores);

        this.setBaseHeight(-0.5f);
        this.setHeightVariation(0f);
    }

    @Override
    public String getName() {
        return "River";
    }
}

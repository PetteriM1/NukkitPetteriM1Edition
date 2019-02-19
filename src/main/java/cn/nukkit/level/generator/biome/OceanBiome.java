package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockGravel;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class OceanBiome extends WateryBiome {

    public OceanBiome() {
        super();
        this.setElevation(40, 60);
        this.temperature = 0.5;
        this.rainfall = 0.5;
    }

    @Override
    public Block[] getGroundCover() {
        return new Block[]{new BlockGravel()};
    }

    @Override
    public String getName() {
        return "Ocean";
    }
}

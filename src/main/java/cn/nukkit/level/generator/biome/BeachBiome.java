package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSand;
import cn.nukkit.block.BlockSandstone;

/**
 * @author PeratX
 * Nukkit Project
 */
public class BeachBiome extends SandyBiome {

    public BeachBiome() {
        this.setElevation(62, 65);
        this.temperature = 2;
        this.rainfall = 0;

        this.setGroundCover(new Block[]{
                new BlockSand(),
                new BlockSand(),
                new BlockSandstone(),
                new BlockSandstone(),
                new BlockSandstone()
        });
    }

    @Override
    public String getName() {
        return "Beach";
    }
}

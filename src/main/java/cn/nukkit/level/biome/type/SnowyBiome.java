package cn.nukkit.level.biome.type;

import cn.nukkit.block.Block;
import cn.nukkit.level.generator.populator.impl.WaterIcePopulator;

/**
 * @author DaPorkchop_
 * Nukkit Project
 */
public abstract class SnowyBiome extends GrassyBiome {

    public SnowyBiome() {
        super();

        this.addPopulator(new WaterIcePopulator());
    }

    @Override
    public int getCoverId(int x, int z) {
        return SNOW_LAYER << Block.DATA_BITS;
    }

    @Override
    public boolean isFreezing() {
        return true;
    }

    @Override
    public boolean canRain() {
        return false;
    }
}

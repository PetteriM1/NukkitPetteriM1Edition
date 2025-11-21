package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockCrimsonNylium extends BlockNylium {

    public BlockCrimsonNylium() {
        // Does nothing
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CRIMSON_NYLIUM_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return CRIMSON_NYLIUM;
    }

    @Override
    public String getName() {
        return "Crimson Nylium";
    }
}

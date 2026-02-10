package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockWarpedNylium extends BlockNylium {

    @Override
    public BlockColor getColor() {
        return BlockColor.WARPED_NYLIUM_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return WARPED_NYLIUM;
    }

    @Override
    public String getName() {
        return "Warped Nylium";
    }
}
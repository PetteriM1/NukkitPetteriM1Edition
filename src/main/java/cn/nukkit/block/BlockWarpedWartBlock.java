package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockWarpedWartBlock extends BlockNetherWartBlock {

    public BlockWarpedWartBlock() {
        super();
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WARPED_WART_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 1;
    }

    @Override
    public int getId() {
        return WARPED_WART_BLOCK;
    }

    @Override
    public String getName() {
        return "Warped Wart Block";
    }

    @Override
    public double getResistance() {
        return 5;
    }
}
package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedSpruce extends BlockWoodStripped {

    public BlockWoodStrippedSpruce() {
        this(0);
    }

    public BlockWoodStrippedSpruce(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SPRUCE_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return STRIPPED_SPRUCE_LOG;
    }

    @Override
    public String getName() {
        return "Stripped Spruce Log";
    }
}

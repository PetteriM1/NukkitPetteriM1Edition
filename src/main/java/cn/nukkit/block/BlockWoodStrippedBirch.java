package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedBirch extends BlockWoodStripped {

    public BlockWoodStrippedBirch() {
        this(0);
    }

    public BlockWoodStrippedBirch(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return STRIPPED_BIRCH_LOG;
    }

    @Override
    public String getName() {
        return "Stripped Birch Log";
    }
}

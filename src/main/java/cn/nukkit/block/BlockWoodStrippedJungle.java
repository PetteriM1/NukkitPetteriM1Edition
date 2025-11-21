package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedJungle extends BlockWoodStripped {

    public BlockWoodStrippedJungle() {
        this(0);
    }

    public BlockWoodStrippedJungle(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return STRIPPED_JUNGLE_LOG;
    }

    @Override
    public String getName() {
        return "Stripped Jungle Log";
    }
}

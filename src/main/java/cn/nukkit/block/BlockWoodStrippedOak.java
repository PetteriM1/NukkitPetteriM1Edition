package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockWoodStrippedOak extends BlockWoodStripped {

    public BlockWoodStrippedOak() {
        this(0);
    }

    public BlockWoodStrippedOak(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return STRIPPED_OAK_LOG;
    }

    @Override
    public String getName() {
        return "Stripped Oak Log";
    }
}

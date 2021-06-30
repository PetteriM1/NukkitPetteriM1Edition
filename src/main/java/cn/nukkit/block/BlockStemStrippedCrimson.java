package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockStemStrippedCrimson extends BlockStemStripped {

    @Override
    public int getId() {
        return STRIPPED_CRIMSON_STEM;
    }

    @Override
    public int getBurnChance() {
        return 0;
    }

    @Override
    public int getBurnAbility() {
        return 0;
    }

    @Override
    public String getName() {
        return "Stripped Crimson Stem";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CRIMSON_STEM_BLOCK_COLOR;
    }
}

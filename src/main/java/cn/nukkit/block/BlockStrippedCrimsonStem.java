package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockStrippedCrimsonStem extends BlockStemStripped {

    public BlockStrippedCrimsonStem() {
        this(0);
    }

    public BlockStrippedCrimsonStem(int meta) {
        super(meta);
    }

    @Override
    public int getBurnAbility() {
        return 0;
    }

    @Override
    public int getBurnChance() {
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CRIMSON_STEM_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return STRIPPED_CRIMSON_STEM;
    }

    @Override
    public String getName() {
        return "Stripped Crimson Stem";
    }
}
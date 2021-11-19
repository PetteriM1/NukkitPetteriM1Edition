package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockStemWarped extends BlockStem {

    public BlockStemWarped() {
        super();
    }

    public BlockStemWarped(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return WARPED_STEM;
    }

    @Override
    public int getStrippedId() {
        return STRIPPED_WARPED_STEM;
    }

    @Override
    public String getName() {
        return "Warped Stem";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WARPED_STEM_BLOCK_COLOR;
    }
}

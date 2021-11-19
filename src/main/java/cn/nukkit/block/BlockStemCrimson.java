package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockStemCrimson extends BlockStem {

    public BlockStemCrimson() {
        super();
    }

    public BlockStemCrimson(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return CRIMSON_STEM;
    }

    @Override
    public int getStrippedId() {
        return STRIPPED_CRIMSON_STEM;
    }

    @Override
    public String getName() {
        return "Crimson Stem";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CRIMSON_STEM_BLOCK_COLOR;
    }
}

package cn.nukkit.block;

import cn.nukkit.utils.DyeColor;

/**
 * Created by CreeperFace on 2.6.2017.
 */
public class BlockTerracottaGlazedGray extends BlockTerracottaGlazed {

    public BlockTerracottaGlazedGray() {
        this(0);
    }

    public BlockTerracottaGlazedGray(int meta) {
        super(meta);
    }

    public DyeColor getDyeColor() {
        return DyeColor.GRAY;
    }

    @Override
    public int getId() {
        return GRAY_GLAZED_TERRACOTTA;
    }

    @Override
    public String getName() {
        return "Gray Glazed Terracotta";
    }
}

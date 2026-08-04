package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockOreCopperDeepslate extends BlockOreCopper {

    public BlockOreCopperDeepslate() {
        // Does nothing
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DEEPSLATE_GRAY_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 4.5;
    }

    @Override
    public int getId() {
        return DEEPSLATE_COPPER_ORE;
    }

    @Override
    public String getName() {
        return "Deepslate Copper Ore";
    }
}

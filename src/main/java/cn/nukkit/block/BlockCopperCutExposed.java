package cn.nukkit.block;

import cn.nukkit.block.properties.OxidizationLevel;
import cn.nukkit.utils.BlockColor;

public class BlockCopperCutExposed extends BlockCopperCut {

    public BlockCopperCutExposed() {
        // Does nothing
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.LIGHT_GRAY_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return EXPOSED_CUT_COPPER;
    }

    @Override
    public String getName() {
        return "Exposed Cut Copper";
    }

    @Override
    public OxidizationLevel getOxidizationLevel() {
        return OxidizationLevel.EXPOSED;
    }
}

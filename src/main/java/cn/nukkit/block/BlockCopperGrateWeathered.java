package cn.nukkit.block;

import cn.nukkit.block.properties.OxidizationLevel;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperGrateWeathered extends BlockCopperGrate {

    public BlockCopperGrateWeathered() {
        // Does nothing
    }

    @Override
    public String getName() {
        return "Weathered Copper Grate";
    }

    @Override
    public int getId() {
        return WEATHERED_COPPER_GRATE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WARPED_STEM_BLOCK_COLOR;
    }

    @Override
    public OxidizationLevel getOxidizationLevel() {
        return OxidizationLevel.WEATHERED;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.WEATHERED_COPPER;
        }
        return BlockTypes.IRON_BLOCK;
    }
}

package cn.nukkit.block;

import cn.nukkit.block.properties.OxidizationLevel;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperChiseledWeathered extends BlockCopperChiseled {

    public BlockCopperChiseledWeathered() {
        // Does nothing
    }

    @Override
    public String getName() {
        return "Weathered Chiseled Copper";
    }

    @Override
    public int getId() {
        return WEATHERED_CHISELED_COPPER;
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

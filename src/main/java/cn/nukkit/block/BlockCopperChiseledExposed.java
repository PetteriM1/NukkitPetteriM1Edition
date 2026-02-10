package cn.nukkit.block;

import cn.nukkit.block.properties.OxidizationLevel;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperChiseledExposed extends BlockCopperChiseled {

    public BlockCopperChiseledExposed() {
        // Does nothing
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.EXPOSED_COPPER;
        }
        return BlockTypes.IRON_BLOCK;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.LIGHT_GRAY_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return EXPOSED_CHISELED_COPPER;
    }

    @Override
    public String getName() {
        return "Exposed Chiseled Copper";
    }

    @Override
    public OxidizationLevel getOxidizationLevel() {
        return OxidizationLevel.EXPOSED;
    }
}

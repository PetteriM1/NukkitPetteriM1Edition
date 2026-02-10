package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperChiseledWeatheredWaxed extends BlockCopperChiseledWeathered {

    public BlockCopperChiseledWeatheredWaxed() {
        // Does nothing
    }

    @Override
    public int getId() {
        return WAXED_WEATHERED_CHISELED_COPPER;
    }

    @Override
    public String getName() {
        return "Waxed Weathered Chiseled Copper";
    }

    @Override
    public boolean isWaxed() {
        return true;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.WAXED_WEATHERED_COPPER;
        }
        return BlockTypes.IRON_BLOCK;
    }
}

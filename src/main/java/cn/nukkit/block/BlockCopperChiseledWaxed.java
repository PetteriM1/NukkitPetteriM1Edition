package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperChiseledWaxed extends BlockCopperChiseled {

    public BlockCopperChiseledWaxed() {
        // Does nothing
    }

    @Override
    public String getName() {
        return "Waxed Chiseled Copper";
    }

    @Override
    public int getId() {
        return WAXED_CHISELED_COPPER;
    }

    @Override
    public boolean isWaxed() {
        return true;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.WAXED_COPPER;
        }
        return BlockTypes.IRON_BLOCK;
    }
}

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperChiseledExposedWaxed extends BlockCopperChiseledExposed {

    public BlockCopperChiseledExposedWaxed() {
        // Does nothing
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.WAXED_EXPOSED_COPPER;
        }
        return BlockTypes.IRON_BLOCK;
    }

    @Override
    public int getId() {
        return WAXED_EXPOSED_CHISELED_COPPER;
    }

    @Override
    public String getName() {
        return "Waxed Exposed Chiseled Copper";
    }

    @Override
    public boolean isWaxed() {
        return true;
    }
}

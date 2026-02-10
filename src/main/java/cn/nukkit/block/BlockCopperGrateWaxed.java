package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperGrateWaxed extends BlockCopperGrate {

    public BlockCopperGrateWaxed() {
        // Does nothing
    }

    @Override
    public int getId() {
        return WAXED_COPPER_GRATE;
    }

    @Override
    public String getName() {
        return "Waxed Copper Grate";
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

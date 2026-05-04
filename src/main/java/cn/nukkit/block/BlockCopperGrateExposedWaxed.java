package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperGrateExposedWaxed extends BlockCopperGrateExposed {

    public BlockCopperGrateExposedWaxed() {
        // Does nothing
    }

    @Override
    public int getId() {
        return WAXED_EXPOSED_COPPER_GRATE;
    }

    @Override
    public String getName() {
        return "Waxed Exposed Copper Grate";
    }

    @Override
    public boolean isWaxed() {
        return true;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.WAXED_EXPOSED_COPPER;
        }
        return BlockTypes.IRON_BLOCK;
    }
}

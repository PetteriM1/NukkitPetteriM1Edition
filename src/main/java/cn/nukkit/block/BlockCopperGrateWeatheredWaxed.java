package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperGrateWeatheredWaxed extends BlockCopperGrateWeathered {

    public BlockCopperGrateWeatheredWaxed() {
        // Does nothing
    }

    @Override
    public int getId() {
        return WAXED_WEATHERED_COPPER_GRATE;
    }

    @Override
    public String getName() {
        return "Waxed Weathered Copper Grate";
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

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperBulbWeatheredWaxed extends BlockCopperBulbWeathered {

    public BlockCopperBulbWeatheredWaxed() {
        // Does nothing
    }

    @Override
    public String getName() {
        return "Waxed Weathered Copper Bulb";
    }

    @Override
    public int getId() {
        return WAXED_WEATHERED_COPPER_BULB;
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

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperGrateOxidizedWaxed extends BlockCopperGrateOxidized {

    public BlockCopperGrateOxidizedWaxed() {
        // Does nothing
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.WAXED_OXIDIZED_COPPER;
        }
        return BlockTypes.IRON_BLOCK;
    }

    @Override
    public int getId() {
        return WAXED_OXIDIZED_COPPER_GRATE;
    }

    @Override
    public String getName() {
        return "Waxed Oxidized Copper Grate";
    }

    @Override
    public boolean isWaxed() {
        return true;
    }
}

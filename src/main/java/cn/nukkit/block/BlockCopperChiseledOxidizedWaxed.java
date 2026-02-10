package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperChiseledOxidizedWaxed extends BlockCopperChiseledOxidized {

    public BlockCopperChiseledOxidizedWaxed() {
        // Does nothing
    }

    @Override
    public int getId() {
        return WAXED_OXIDIZED_CHISELED_COPPER;
    }

    @Override
    public String getName() {
        return "Waxed Oxidized Chiseled Copper";
    }

    @Override
    public boolean isWaxed() {
        return true;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.WAXED_OXIDIZED_COPPER;
        }
        return BlockTypes.IRON_BLOCK;
    }
}

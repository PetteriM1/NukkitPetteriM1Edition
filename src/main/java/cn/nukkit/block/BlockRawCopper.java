package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockRawCopper extends BlockRawOreVariant {

    public BlockRawCopper() {
    }

    @Override
    public int getId() {
        return RAW_COPPER_BLOCK;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Block of Raw Copper";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.IRON_ORE;
    }
}

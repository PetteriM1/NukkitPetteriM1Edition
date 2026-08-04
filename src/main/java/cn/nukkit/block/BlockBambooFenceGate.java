package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockBambooFenceGate extends BlockFenceGate {

    public BlockBambooFenceGate() {
        this(0);
    }

    public BlockBambooFenceGate(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BAMBOO_FENCE_GATE;
    }

    @Override
    public String getName() {
        return "Bamboo Fence Gate";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.YELLOW_BLOCK_COLOR;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.FENCE_GATE;
    }
}

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockFenceGateCrimson extends BlockFenceGate {

    public BlockFenceGateCrimson() {
        this(0);
    }

    public BlockFenceGateCrimson(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return CRIMSON_FENCE_GATE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_16_0;
    }

    @Override
    public String getName() {
        return "Crimson Fence Gate";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.FENCE_GATE;
    }
}

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockButtonWarped extends BlockButtonWooden {

    public BlockButtonWarped() {
        this(0);
    }

    public BlockButtonWarped(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_BUTTON;
    }

    @Override
    public int getId() {
        return WARPED_BUTTON;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_16_0;
    }

    @Override
    public String getName() {
        return "Warped Button";
    }
}

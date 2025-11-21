package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockButtonMangrove extends BlockButtonWooden {

    public BlockButtonMangrove() {
        this(0);
    }

    public BlockButtonMangrove(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_BUTTON;
    }

    @Override
    public int getId() {
        return MANGROVE_BUTTON;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_19_0;
    }

    @Override
    public String getName() {
        return "Mangrove Button";
    }
}

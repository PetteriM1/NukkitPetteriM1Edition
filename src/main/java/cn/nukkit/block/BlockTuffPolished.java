package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockTuffPolished extends BlockTuff {

    public BlockTuffPolished() {
        // Does Nothing
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.STONE;
        }
        return BlockTypes.POLISHED_DEEPSLATE;
    }

    @Override
    public int getId() {
        return POLISHED_TUFF;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Polished Tuff";
    }
}

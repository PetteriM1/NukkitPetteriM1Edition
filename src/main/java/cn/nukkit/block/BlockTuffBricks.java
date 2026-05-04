package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockTuffBricks extends BlockTuff {

    public BlockTuffBricks() {
        // Does Nothing
    }

    @Override
    public int getId() {
        return TUFF_BRICKS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Tuff Bricks";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.STONE_BRICKS;
        }
        return BlockTypes.DEEPSLATE_BRICKS;
    }
}

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockChiseledTuffBricks extends BlockTuff {

    public BlockChiseledTuffBricks() {
        // Does Nothing
    }

    @Override
    public int getId() {
        return CHISELED_TUFF_BRICKS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Chiseled Tuff Bricks";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.STONE;
        }
        return BlockTypes.DEEPSLATE_BRICKS;
    }
}

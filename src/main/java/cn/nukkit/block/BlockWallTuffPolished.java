package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockWallTuffPolished extends BlockWall {

    public BlockWallTuffPolished() {
        this(0);
    }

    public BlockWallTuffPolished(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return POLISHED_TUFF_WALL;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Polished Tuff Wall";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.STONE_WALL;
        }
        return BlockTypes.POLISHED_DEEPSLATE_WALL;
    }
}

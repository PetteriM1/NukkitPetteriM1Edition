package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockWallDeepslatePolished extends BlockWall {

    public BlockWallDeepslatePolished() {
        this(0);
    }

    public BlockWallDeepslatePolished(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Polished Deepslate Wall";
    }

    @Override
    public int getId() {
        return POLISHED_DEEPSLATE_WALL;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.STONE_WALL;
    }
}

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockWallBrickDeepslate extends BlockWall {

    public BlockWallBrickDeepslate() {
        this(0);
    }

    public BlockWallBrickDeepslate(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DEEPSLATE_BRICK_WALL;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Deepslate Brick Wall";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.STONE_WALL;
    }
}

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockWallTuffBrick extends BlockWall {

    public BlockWallTuffBrick() {
        this(0);
    }

    public BlockWallTuffBrick(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Tuff Brick Wall";
    }

    @Override
    public int getId() {
        return TUFF_BRICK_WALL;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.STONE_WALL;
        }
        return BlockTypes.DEEPSLATE_BRICK_WALL;
    }
}

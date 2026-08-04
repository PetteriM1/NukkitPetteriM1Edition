package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockWallTileDeepslate extends BlockWall {

    public BlockWallTileDeepslate() {
        this(0);
    }

    public BlockWallTileDeepslate(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DEEPSLATE_TILE_WALL;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Deepslate Tile Wall";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.STONE_WALL;
    }
}

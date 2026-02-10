package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockWallTuff extends BlockWall {

    public BlockWallTuff() {
        this(0);
    }

    public BlockWallTuff(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.STONE_WALL;
        }
        return BlockTypes.COBBLED_DEEPSLATE_WALL;
    }

    @Override
    public int getId() {
        return TUFF_WALL;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Tuff Wall";
    }
}

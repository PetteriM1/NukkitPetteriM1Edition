package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockWallDeepslateCobbled extends BlockWall {

    public BlockWallDeepslateCobbled() {
        this(0);
    }

    public BlockWallDeepslateCobbled(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Cobbled Deepslate Wall";
    }

    @Override
    public int getId() {
        return COBBLED_DEEPSLATE_WALL;
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

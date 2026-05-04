package cn.nukkit.block.properties;

import cn.nukkit.block.BlockMeta;
import cn.nukkit.block.BlockTypes;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockNotImplemented extends BlockMeta {

    private final int id;

    public BlockNotImplemented(int id) {
        this(id, 0);
    }

    public BlockNotImplemented(int id, int meta) {
        super(meta);
        this.id = id;
    }

    @Override
    public double getHardness() {
        return 0.1;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.CURRENT_PROTOCOL;
    }

    @Override
    public String getName() {
        return "Not Implemented";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.INFO_UPDATE;
    }
}

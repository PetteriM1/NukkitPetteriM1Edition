package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockBambooTrapdoor extends BlockTrapdoor {

    public BlockBambooTrapdoor() {
        this(0);
    }

    public BlockBambooTrapdoor(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Bamboo Trapdoor";
    }

    @Override
    public int getId() {
        return BAMBOO_TRAPDOOR;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.TRAPDOOR;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.YELLOW_BLOCK_COLOR;
    }
}

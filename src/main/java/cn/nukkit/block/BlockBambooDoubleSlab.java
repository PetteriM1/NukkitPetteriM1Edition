package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockBambooDoubleSlab extends BlockDoubleSlabWood {

    public BlockBambooDoubleSlab() {
        this(0);
    }

    public BlockBambooDoubleSlab(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DOUBLE_WOOD_SLAB;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.YELLOW_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return BAMBOO_DOUBLE_SLAB;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Bamboo Double Slab";
    }

    @Override
    public int getSingleSlabId() {
        return BAMBOO_SLAB;
    }
}

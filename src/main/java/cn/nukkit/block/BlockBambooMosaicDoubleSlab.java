package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockBambooMosaicDoubleSlab extends BlockDoubleSlabWood {

    public BlockBambooMosaicDoubleSlab() {
        this(0);
    }

    public BlockBambooMosaicDoubleSlab(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Bamboo Mosaic Double Slab";
    }

    @Override
    public int getId() {
        return BAMBOO_MOSAIC_DOUBLE_SLAB;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DOUBLE_WOOD_SLAB;
    }

    @Override
    public int getSingleSlabId() {
        return BAMBOO_MOSAIC_SLAB;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.YELLOW_BLOCK_COLOR;
    }
}

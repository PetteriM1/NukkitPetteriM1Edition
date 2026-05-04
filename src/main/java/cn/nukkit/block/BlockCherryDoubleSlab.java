package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCherryDoubleSlab extends BlockDoubleSlabWood {

    public BlockCherryDoubleSlab() {
        this(0);
    }

    public BlockCherryDoubleSlab(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WHITE_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return CHERRY_DOUBLE_SLAB;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Cherry Double Slab";
    }

    @Override
    public int getSingleSlabId() {
        return CHERRY_SLAB;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DOUBLE_WOOD_SLAB;
    }
}

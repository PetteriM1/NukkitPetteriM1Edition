package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockStairsMangrove extends BlockStairsWood {

    public BlockStairsMangrove() {
        this(0);
    }

    public BlockStairsMangrove(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return MANGROVE_STAIRS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_19_0;
    }

    @Override
    public String getName() {
        return "Mangrove Stairs";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.OAK_WOOD_STAIRS;
    }
}

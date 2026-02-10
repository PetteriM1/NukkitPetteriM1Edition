package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCherryStairs extends BlockStairsWood {

    public BlockCherryStairs() {
        this(0);
    }

    public BlockCherryStairs(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_STAIRS;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WHITE_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return CHERRY_STAIRS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Cherry Stairs";
    }
}

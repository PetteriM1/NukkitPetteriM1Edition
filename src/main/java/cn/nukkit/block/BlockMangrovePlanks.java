package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockMangrovePlanks extends BlockPlanks {

    public BlockMangrovePlanks() {
        this(0);
    }

    public BlockMangrovePlanks(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return MANGROVE_PLANKS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Mangrove Planks";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.PLANKS;
    }
}

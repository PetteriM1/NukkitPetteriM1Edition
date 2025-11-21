package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockBambooPlanks extends BlockPlanks {

    public BlockBambooPlanks() {
        this(0);
    }

    public BlockBambooPlanks(int meta) {
        super(0);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.PLANKS;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.YELLOW_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return BAMBOO_PLANKS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Bamboo Planks";
    }
}
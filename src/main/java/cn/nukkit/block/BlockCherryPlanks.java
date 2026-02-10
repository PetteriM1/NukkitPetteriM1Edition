package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCherryPlanks extends BlockPlanks {

    public BlockCherryPlanks() {
        this(0);
    }

    public BlockCherryPlanks(int meta) {
        super(0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WHITE_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return CHERRY_PLANKS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Cherry Planks";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.PLANKS;
    }
}
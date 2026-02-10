package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockWarpedPlanks extends BlockSolid {

    public BlockWarpedPlanks() {
        this(0);
    }

    public BlockWarpedPlanks(int meta) {
        // super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WARPED_STEM_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public int getId() {
        return WARPED_PLANKS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_16_0;
    }

    @Override
    public String getName() {
        return "Warped Planks";
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.PLANKS;
    }
}
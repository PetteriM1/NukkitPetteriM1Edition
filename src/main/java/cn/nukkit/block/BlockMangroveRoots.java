package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockMangroveRoots extends BlockTransparent {

    @Override
    public int getBurnAbility() {
        return 60;
    }

    @Override
    public int getBurnChance() {
        return 30;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SPRUCE_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 0.7;
    }

    @Override
    public int getId() {
        return MANGROVE_ROOTS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_19_0;
    }

    @Override
    public String getName() {
        return "Mangrove Roots";
    }

    @Override
    public double getResistance() {
        return 3.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public WaterloggingType getWaterloggingType() {
        return WaterloggingType.WHEN_PLACED_IN_WATER;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.LEAVES;
    }
}

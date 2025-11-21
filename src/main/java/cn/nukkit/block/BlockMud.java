package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockMud extends BlockSolid {

    public BlockMud() {
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DIRT;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CYAN_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public int getId() {
        return MUD;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_19_0;
    }

    @Override
    public String getName() {
        return "Mud";
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }
}

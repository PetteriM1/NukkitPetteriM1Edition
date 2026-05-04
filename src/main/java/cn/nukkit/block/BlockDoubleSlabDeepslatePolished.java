package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockDoubleSlabDeepslatePolished extends BlockDoubleSlabBase {

    public BlockDoubleSlabDeepslatePolished() {
        this(0);
    }

    public BlockDoubleSlabDeepslatePolished(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DEEPSLATE_GRAY_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public int getId() {
        return POLISHED_DEEPSLATE_DOUBLE_SLAB;
    }

    @Override
    public int getItemDamage() {
        return 0;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public double getResistance() {
        return 6;
    }

    @Override
    public int getSingleSlabId() {
        return POLISHED_DEEPSLATE_SLAB;
    }

    @Override
    public String getSlabName() {
        return "Double Polished Deepslate Slab";
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DOUBLE_STONE_SLAB;
    }
}

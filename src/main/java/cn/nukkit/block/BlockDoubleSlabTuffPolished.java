package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockDoubleSlabTuffPolished extends BlockDoubleSlabBase {

    public BlockDoubleSlabTuffPolished() {
        this(0);
    }

    public BlockDoubleSlabTuffPolished(int meta) {
        super(meta);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.DOUBLE_SLAB;
        }
        return BlockTypes.POLISHED_DEEPSLATE_DOUBLE_SLAB;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRAY_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public int getId() {
        return POLISHED_TUFF_DOUBLE_SLAB;
    }

    @Override
    public int getItemDamage() {
        return 0;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public double getResistance() {
        return 6;
    }

    @Override
    public int getSingleSlabId() {
        return POLISHED_TUFF_SLAB;
    }

    @Override
    public String getSlabName() {
        return "Polished Tuff Slab";
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
}

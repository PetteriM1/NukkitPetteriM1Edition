package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockDoubleSlabTuff extends BlockDoubleSlabBase {

    public BlockDoubleSlabTuff() {
        this(0);
    }

    public BlockDoubleSlabTuff(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return TUFF_DOUBLE_SLAB;
    }

    @Override
    public int getSingleSlabId() {
        return TUFF_SLAB;
    }

    @Override
    public int getItemDamage() {
        return 0;
    }

    @Override
    public String getSlabName() {
        return "Tuff Slab";
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public double getResistance() {
        return 6;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
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
    public BlockColor getColor() {
        return BlockColor.GRAY_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.DOUBLE_SLAB;
        }
        return BlockTypes.COBBLED_DEEPSLATE_DOUBLE_SLAB;
    }
}

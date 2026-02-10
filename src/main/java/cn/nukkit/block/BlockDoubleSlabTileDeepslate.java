package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockDoubleSlabTileDeepslate extends BlockDoubleSlabBase {

    public BlockDoubleSlabTileDeepslate() {
        this(0);
    }

    protected BlockDoubleSlabTileDeepslate(int meta) {
        super(meta);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DOUBLE_STONE_SLAB;
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
        return DEEPSLATE_TILE_DOUBLE_SLAB;
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
        return DEEPSLATE_TILE_SLAB;
    }

    @Override
    public String getSlabName() {
        return "Double Deepslate Tile Slab";
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

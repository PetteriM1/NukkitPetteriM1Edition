package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockSlabTileDeepslate extends BlockSlab {

    public BlockSlabTileDeepslate() {
        this(0);
    }

    public BlockSlabTileDeepslate(int meta) {
        super(meta, DEEPSLATE_TILE_SLAB);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.STONE_SLAB;
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
        return DEEPSLATE_TILE_SLAB;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getSlabName() {
        return "Deepslate Tile";
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

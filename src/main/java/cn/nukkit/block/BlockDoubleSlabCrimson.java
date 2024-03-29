package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

public class BlockDoubleSlabCrimson extends BlockDoubleSlabBase {

    public BlockDoubleSlabCrimson() {
        super(0);
    }

    public BlockDoubleSlabCrimson(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return CRIMSON_DOUBLE_SLAB;
    }

    @Override
    public String getSlabName() {
        return "Crimson";
    }

    @Override
    public int getSingleSlabId() {
        return CRIMSON_SLAB;
    }

    @Override
    public int getItemDamage() {
        return 0;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }

    @Override
    public int getBurnChance() {
        return 0;
    }

    @Override
    public int getBurnAbility() {
        return 0;
    }
}

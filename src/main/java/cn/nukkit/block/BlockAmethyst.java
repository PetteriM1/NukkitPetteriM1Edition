package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.material.BlockType;

public class BlockAmethyst extends BlockSolid {

    public BlockAmethyst() {
        // Does nothing
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public int getId() {
        return AMETHYST_BLOCK;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Amethyst Block";
    }

    @Override
    public double getResistance() {
        return 7.5;
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
        return BlockTypes.CONCRETE;
    }

    @Override
    public int getAlternateMeta(int protocol) {
        return DyeColor.PURPLE.getWoolData();
    }

    // TODO:
    /*@Override
    public boolean isLavaResistant() {
        return true;
    }*/
}

package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockDripstone extends BlockSolid {

    public BlockDripstone() {
        // Does nothing
    }

    @Override
    public String getName() {
        return "Dripstone Block";
    }

    @Override
    public int getId() {
        return DRIPSTONE_BLOCK;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 5;
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
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.STONE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_TERRACOTA_BLOCK_COLOR;
    }

    // TODO:
    /*@Override
    public boolean isLavaResistant() {
        return true;
    }*/
}
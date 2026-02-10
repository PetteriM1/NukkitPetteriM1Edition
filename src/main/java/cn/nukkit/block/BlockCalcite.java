package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCalcite extends BlockSolid {

    public BlockCalcite() {
        // Does nothing
    }

    @Override
    public double getHardness() {
        return 0.75;
    }

    @Override
    public int getId() {
        return CALCITE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Calcite";
    }

    @Override
    public double getResistance() {
        return 3.75;
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
        return BlockTypes.STONE;
    }

    @Override
    public int getAlternateMeta(int protocol) {
        return BlockStone.DIORITE;
    }

    // TODO:
    /*@Override
    public boolean isLavaResistant() {
        return true;
    }*/
}

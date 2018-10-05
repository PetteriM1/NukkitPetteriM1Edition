package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * Created by PetteriM1
 */
public class BlockChemistryTable extends BlockSolid {

    public BlockChemistryTable() {
    }

    @Override
    public int getId() {
        return CHEMISTRY_TABLE;
    }

    @Override
    public String getName() {
        return "Compound Creator";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getResistance() {
        return 10;
    }
}

package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * Created by PetteriM1
 */
public class BlockChemicalHeat extends BlockSolid {

    @Override
    public int getId() {
        return CHEMICAL_HEAT;
    }

    @Override
    public String getName() {
        return "Heat Block";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
}

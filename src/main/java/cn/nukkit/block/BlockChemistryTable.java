package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;

/**
 * Created by PetteriM1
 */
public class BlockChemistryTable extends BlockSolidMeta {

    public BlockChemistryTable() {
        this(0);
    }

    public BlockChemistryTable(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return CHEMISTRY_TABLE;
    }

    @Override
    public String getName() {
        int meta = this.getDamage();
        if (meta >= 12) {
            return "Lab Table";
        } else if (meta >= 8) {
            return "Element Constructor";
        } else if (meta >= 4) {
            return "Material Reducer";
        }
        return "Compound Creator";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getResistance() {
        return this.getDamage() >= 12 ? 3 : 2;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, (this.getDamage() >> 2) << 2);
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setDamage(((this.getDamage() >> 2) << 2) + (player != null ? player.getDirection().getHorizontalIndex() : 0));
        return this.getLevel().setBlock(block, this, true);
    }
}

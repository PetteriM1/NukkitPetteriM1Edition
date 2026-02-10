package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

/**
 * Created by CreeperFace on 2.6.2017.
 */
public class BlockConcrete extends BlockSolidMeta {

    public BlockConcrete() {
        this(0);
    }

    public BlockConcrete(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return DyeColor.getByWoolData(getDamage()).getColor();
    }

    @Override
    public Item[] getDrops(Item item) {
        return item.getTier() >= ItemTool.TIER_WOODEN ? new Item[]{toItem()} : new Item[0];
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(getDamage());
    }

    @Override
    public double getHardness() {
        return 1.8;
    }

    @Override
    public int getId() {
        return CONCRETE;
    }

    @Override
    public String getName() {
        return getDyeColor().getName() + " Concrete";
    }

    @Override
    public double getResistance() {
        return 9;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
}

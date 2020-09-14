package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemCarrot;
import cn.nukkit.utils.Utils;

/**
 * @author Nukkit Project Team
 */
public class BlockCarrot extends BlockCrops {

    public BlockCarrot(int meta) {
        super(meta);
    }

    public BlockCarrot() {
        this(0);
    }

    @Override
    public String getName() {
        return "Carrot Block";
    }

    @Override
    public int getId() {
        return CARROT_BLOCK;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (getDamage() >= 0x07) {
            return new Item[]{
                    new ItemCarrot(0, Utils.rand(1, 5))
            };
        }
        return new Item[]{
                new ItemCarrot()
        };
    }

    @Override
    public Item toItem() {
        return new ItemCarrot();
    }
}

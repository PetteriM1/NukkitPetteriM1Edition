package cn.nukkit.block;

import cn.nukkit.item.Item;

/**
 * Created on 2015/12/1 by xtypr.
 * Package cn.nukkit.block in project Nukkit.
 */
public class BlockLeaves2 extends BlockLeaves {

    public static final int ACACIA = 0;
    public static final int DARK_OAK = 1;

    private static final String[] names = {
            "Acacia Leaves",
            "Dark Oak Leaves"
    };

    public BlockLeaves2() {
        this(0);
    }

    public BlockLeaves2(int meta) {
        super(meta);
    }

    @Override
    protected boolean canDropApple() {
        return (this.getDamage() & 0x01) == DARK_OAK;
    }

    @Override
    public int getId() {
        return LEAVES2;
    }

    public String getName() {
        return names[this.getDamage() & 0x01];
    }

    @Override
    protected Item getSapling() {
        return Item.get(BlockID.SAPLING, (this.getDamage() & 0x01) + 4);
    }
}

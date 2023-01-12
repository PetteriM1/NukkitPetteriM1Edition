/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockLeaves;
import cn.nukkit.item.Item;

public class BlockLeaves2
extends BlockLeaves {
    public static final int ACACIA = 0;
    public static final int DARK_OAK = 1;
    private static final String[] d = new String[]{"Acacia Leaves", "Dark Oak Leaves"};

    public BlockLeaves2() {
        this(0);
    }

    public BlockLeaves2(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return d[this.getDamage() & 1];
    }

    @Override
    public int getId() {
        return 161;
    }

    @Override
    protected boolean canDropApple() {
        return (this.getDamage() & 1) == 1;
    }

    @Override
    protected Item getSapling() {
        return Item.get(6, (this.getDamage() & 1) + 4);
    }
}


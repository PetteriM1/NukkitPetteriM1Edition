/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWallSign;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignBirch;

public class BlockBirchWallSign
extends BlockWallSign {
    public BlockBirchWallSign() {
        this(0);
    }

    public BlockBirchWallSign(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Birch Wall Sign";
    }

    @Override
    public int getId() {
        return 442;
    }

    @Override
    public Item toItem() {
        return new ItemSignBirch();
    }

    @Override
    protected int getPostId() {
        return 441;
    }

    @Override
    protected int getWallId() {
        return 442;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWallSign;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignAcacia;

public class BlockAcaciaWallSign
extends BlockWallSign {
    public BlockAcaciaWallSign() {
        this(0);
    }

    public BlockAcaciaWallSign(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Acacia Wall Sign";
    }

    @Override
    public int getId() {
        return 446;
    }

    @Override
    public Item toItem() {
        return new ItemSignAcacia();
    }

    @Override
    protected int getPostId() {
        return 445;
    }

    @Override
    protected int getWallId() {
        return 446;
    }
}


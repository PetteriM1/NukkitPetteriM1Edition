/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWallSign;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignDarkOak;

public class BlockDarkOakWallSign
extends BlockWallSign {
    public BlockDarkOakWallSign() {
        this(0);
    }

    public BlockDarkOakWallSign(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Dark Oak Wall Sign";
    }

    @Override
    public int getId() {
        return 448;
    }

    @Override
    public Item toItem() {
        return new ItemSignDarkOak();
    }

    @Override
    protected int getPostId() {
        return 447;
    }

    @Override
    protected int getWallId() {
        return 448;
    }
}


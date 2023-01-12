/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSignPost;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignDarkOak;

public class BlockDarkOakSignStanding
extends BlockSignPost {
    public BlockDarkOakSignStanding() {
        this(0);
    }

    public BlockDarkOakSignStanding(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Dark Oak Sign Post";
    }

    @Override
    public int getId() {
        return 447;
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


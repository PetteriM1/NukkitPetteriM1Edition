/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSignPost;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignBirch;

public class BlockBirchSignStanding
extends BlockSignPost {
    public BlockBirchSignStanding() {
        this(0);
    }

    public BlockBirchSignStanding(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Birch Sign Post";
    }

    @Override
    public int getId() {
        return 441;
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


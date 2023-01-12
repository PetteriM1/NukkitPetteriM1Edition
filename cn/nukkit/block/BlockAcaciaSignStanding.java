/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSignPost;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignAcacia;

public class BlockAcaciaSignStanding
extends BlockSignPost {
    public BlockAcaciaSignStanding() {
        this(0);
    }

    public BlockAcaciaSignStanding(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Acacia Sign Post";
    }

    @Override
    public int getId() {
        return 445;
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


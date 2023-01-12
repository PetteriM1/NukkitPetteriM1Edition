/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSignPost;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignSpruce;

public class BlockSpruceSignStanding
extends BlockSignPost {
    public BlockSpruceSignStanding() {
        this(0);
    }

    public BlockSpruceSignStanding(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Spruce Sign Post";
    }

    @Override
    public int getId() {
        return 436;
    }

    @Override
    public Item toItem() {
        return new ItemSignSpruce();
    }

    @Override
    protected int getPostId() {
        return 436;
    }

    @Override
    protected int getWallId() {
        return 437;
    }
}


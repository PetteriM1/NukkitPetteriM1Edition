/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSignPost;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignJungle;

public class BlockJungleSignStanding
extends BlockSignPost {
    public BlockJungleSignStanding() {
        this(0);
    }

    public BlockJungleSignStanding(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Jungle Sign Post";
    }

    @Override
    public int getId() {
        return 443;
    }

    @Override
    public Item toItem() {
        return new ItemSignJungle();
    }

    @Override
    protected int getPostId() {
        return 443;
    }

    @Override
    protected int getWallId() {
        return 444;
    }
}


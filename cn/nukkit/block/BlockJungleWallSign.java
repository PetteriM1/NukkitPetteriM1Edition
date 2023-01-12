/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWallSign;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignJungle;

public class BlockJungleWallSign
extends BlockWallSign {
    public BlockJungleWallSign() {
        this(0);
    }

    public BlockJungleWallSign(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Jungle Wall Sign";
    }

    @Override
    public int getId() {
        return 444;
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


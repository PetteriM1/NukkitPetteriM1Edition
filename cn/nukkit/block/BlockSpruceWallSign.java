/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockWallSign;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSignSpruce;

public class BlockSpruceWallSign
extends BlockWallSign {
    public BlockSpruceWallSign() {
        this(0);
    }

    public BlockSpruceWallSign(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Spruce Wall Sign";
    }

    @Override
    public int getId() {
        return 437;
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


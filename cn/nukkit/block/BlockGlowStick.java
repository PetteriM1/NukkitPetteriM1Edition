/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;

public class BlockGlowStick
extends BlockTransparentMeta {
    public BlockGlowStick() {
        this(0);
    }

    public BlockGlowStick(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 166;
    }

    @Override
    public String getName() {
        return "Glow Stick";
    }

    @Override
    public Item toItem() {
        return Item.get(0);
    }
}


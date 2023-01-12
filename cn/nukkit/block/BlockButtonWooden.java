/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockButton;

public class BlockButtonWooden
extends BlockButton {
    public BlockButtonWooden() {
        this(0);
    }

    public BlockButtonWooden(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 143;
    }

    @Override
    public String getName() {
        return "Wooden Button";
    }

    @Override
    public int getToolType() {
        return 4;
    }
}


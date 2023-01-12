/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockButtonWooden;

public class BlockButtonAcacia
extends BlockButtonWooden {
    public BlockButtonAcacia() {
        this(0);
    }

    public BlockButtonAcacia(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Acacia Button";
    }

    @Override
    public int getId() {
        return 395;
    }
}


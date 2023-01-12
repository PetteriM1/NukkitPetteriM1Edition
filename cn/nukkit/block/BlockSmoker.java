/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSmokerLit;

public class BlockSmoker
extends BlockSmokerLit {
    public BlockSmoker() {
        this(0);
    }

    public BlockSmoker(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Smoker";
    }

    @Override
    public int getId() {
        return 453;
    }

    @Override
    public int getLightLevel() {
        return 0;
    }
}


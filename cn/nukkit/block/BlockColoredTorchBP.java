/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTorch;

public class BlockColoredTorchBP
extends BlockTorch {
    public BlockColoredTorchBP() {
        this(0);
    }

    public BlockColoredTorchBP(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Blue Torch";
    }

    @Override
    public int getId() {
        return 204;
    }
}


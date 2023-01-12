/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTorch;

public class BlockColoredTorchRG
extends BlockTorch {
    public BlockColoredTorchRG() {
        this(0);
    }

    public BlockColoredTorchRG(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Red Torch";
    }

    @Override
    public int getId() {
        return 202;
    }
}


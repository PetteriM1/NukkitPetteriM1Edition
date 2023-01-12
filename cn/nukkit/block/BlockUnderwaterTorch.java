/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTorch;

public class BlockUnderwaterTorch
extends BlockTorch {
    public BlockUnderwaterTorch() {
        this(0);
    }

    public BlockUnderwaterTorch(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Underwater Torch";
    }

    @Override
    public int getId() {
        return 239;
    }

    @Override
    public int onUpdate(int n) {
        return 0;
    }

    @Override
    public boolean canBeFlowedInto() {
        return false;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockPumpkin;

public class BlockPumpkinLit
extends BlockPumpkin {
    public BlockPumpkinLit() {
        this(0);
    }

    public BlockPumpkinLit(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Jack o'Lantern";
    }

    @Override
    public int getId() {
        return 91;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }
}


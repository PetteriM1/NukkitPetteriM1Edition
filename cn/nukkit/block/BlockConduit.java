/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolidMeta;

public class BlockConduit
extends BlockSolidMeta {
    public BlockConduit() {
        this(0);
    }

    public BlockConduit(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Conduit";
    }

    @Override
    public int getId() {
        return 412;
    }

    @Override
    public double getResistance() {
        return 3.0;
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


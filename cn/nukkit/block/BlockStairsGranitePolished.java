/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsGranite;

public class BlockStairsGranitePolished
extends BlockStairsGranite {
    public BlockStairsGranitePolished() {
        this(0);
    }

    public BlockStairsGranitePolished(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Polished Granite Stairs";
    }

    @Override
    public int getId() {
        return 427;
    }
}


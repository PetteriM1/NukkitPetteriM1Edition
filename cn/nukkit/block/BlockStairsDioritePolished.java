/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockStairsDiorite;

public class BlockStairsDioritePolished
extends BlockStairsDiorite {
    public BlockStairsDioritePolished() {
        this(0);
    }

    public BlockStairsDioritePolished(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Polished Diorite Stairs";
    }

    @Override
    public int getId() {
        return 428;
    }
}


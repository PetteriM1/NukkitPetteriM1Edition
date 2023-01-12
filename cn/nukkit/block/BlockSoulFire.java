/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFire;
import cn.nukkit.utils.BlockColor;

public class BlockSoulFire
extends BlockFire {
    public BlockSoulFire() {
        this(0);
    }

    public BlockSoulFire(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 492;
    }

    @Override
    public String getName() {
        return "Soul Fire";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.LIGHT_BLUE_BLOCK_COLOR;
    }
}


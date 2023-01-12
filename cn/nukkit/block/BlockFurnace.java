/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockFurnaceBurning;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

public class BlockFurnace
extends BlockFurnaceBurning
implements Faceable {
    public BlockFurnace() {
        this(0);
    }

    public BlockFurnace(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Furnace";
    }

    @Override
    public int getId() {
        return 61;
    }

    @Override
    public int getLightLevel() {
        return 0;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.BlockDispenser;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public interface DispenseBehavior {
    public Item dispense(BlockDispenser var1, BlockFace var2, Item var3);
}


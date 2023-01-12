/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class FlintAndSteelDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Block block = blockDispenser.getSide(blockFace);
        if (block.getId() == 0) {
            blockDispenser.level.setBlock(block, Block.get(51));
        } else if (block.getId() == 46) {
            block.onActivate(item);
        } else {
            this.success = false;
        }
        return null;
    }
}


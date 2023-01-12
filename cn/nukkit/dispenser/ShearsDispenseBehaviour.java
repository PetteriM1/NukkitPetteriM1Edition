/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntitySheep;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

public class ShearsDispenseBehaviour
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Block block = blockDispenser.getSide(blockFace);
        item = item.clone();
        for (Entity entity : blockDispenser.getLevel().getNearbyEntities(new AxisAlignedBB(block.x, block.y, block.z, block.x + 1.0, block.y + 1.0, block.z + 1.0))) {
            if (!(entity instanceof EntitySheep) || ((EntitySheep)entity).isSheared()) continue;
            ((EntitySheep)entity).shear(true);
            item.useOn(entity);
            return item.getDamage() >= item.getMaxDurability() ? null : item;
        }
        return item;
    }
}


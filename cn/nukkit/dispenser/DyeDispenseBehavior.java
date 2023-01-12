/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCrops;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.block.BlockDoublePlant;
import cn.nukkit.block.BlockMushroom;
import cn.nukkit.block.BlockSapling;
import cn.nukkit.block.BlockTallGrass;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.DyeColor;

public class DyeDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Block block = blockDispenser.getSide(blockFace);
        if (DyeColor.getByDyeData(item.getDamage()) == DyeColor.WHITE) {
            if (block instanceof BlockCrops || block instanceof BlockSapling || block instanceof BlockTallGrass || block instanceof BlockDoublePlant || block instanceof BlockMushroom) {
                block.onActivate(item);
            } else {
                this.success = false;
            }
            return null;
        }
        return super.dispense(blockDispenser, blockFace, item);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBucket;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.BlockFace;

public class BucketDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Block block = blockDispenser.getSide(blockFace);
        if (item.getDamage() > 0) {
            Block block2;
            if (block.canBeFlowedInto() && (block2 = Block.get(ItemBucket.getDamageByTarget(item.getDamage()))) instanceof BlockLiquid) {
                if (blockDispenser.level.getDimension() == 1) {
                    block2 = Block.get(0);
                    blockDispenser.level.addParticle(new SmokeParticle(block.add(0.5, 0.5, 0.5)), null, 4);
                }
                blockDispenser.level.setBlock(block, block2);
                return Item.get(325);
            }
        } else if (block instanceof BlockLiquid && block.getDamage() == 0) {
            block.level.setBlock(block, Block.get(0));
            return new ItemBucket((Integer)ItemBucket.getDamageByTarget(block.getId()));
        }
        return super.dispense(blockDispenser, blockFace, item);
    }
}


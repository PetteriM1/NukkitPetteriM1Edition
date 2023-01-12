/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.block.BlockWater;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityBoat;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;

public class BoatDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Position position = blockDispenser.getSide(blockFace).multiply(1.125);
        Block block = blockDispenser.getSide(blockFace);
        if (block instanceof BlockWater) {
            position.y += 1.0;
        } else if (block.getId() != 0 || !(block.down() instanceof BlockWater)) {
            return super.dispense(blockDispenser, blockFace, item);
        }
        position = block.getLocation().setYaw(blockFace.getHorizontalAngle());
        EntityBoat entityBoat = new EntityBoat(blockDispenser.level.getChunk(block.getChunkX(), block.getChunkZ()), Entity.getDefaultNBT(position).putByte("woodID", item.getDamage()));
        entityBoat.spawnToAll();
        return null;
    }
}


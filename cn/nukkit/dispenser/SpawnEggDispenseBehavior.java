/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.BlockDispenser;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;

public class SpawnEggDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Position position = blockDispenser.getSide(blockFace).add(0.5, 0.7, 0.5);
        Entity entity = Entity.createEntity(item.getDamage(), (FullChunk)blockDispenser.level.getChunk(position.getChunkX(), position.getChunkZ()), Entity.getDefaultNBT(position), new Object[0]);
        boolean bl = this.success = entity != null;
        if (this.success) {
            if (item.hasCustomName() && entity instanceof EntityLiving) {
                entity.setNameTag(item.getCustomName());
            }
            entity.spawnToAll();
            return null;
        }
        return super.dispense(blockDispenser, blockFace, item);
    }
}


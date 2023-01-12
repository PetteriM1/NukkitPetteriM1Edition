/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.BlockDispenser;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

public class FireChargeDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Vector3 vector3 = blockDispenser.getDispensePosition();
        Entity entity = Entity.createEntity("GhastFireBall", (FullChunk)blockDispenser.level.getChunk(vector3.getChunkX(), vector3.getChunkZ()), Entity.getDefaultNBT(vector3), new Object[0]);
        if (!(entity instanceof EntityProjectile)) {
            return super.dispense(blockDispenser, blockFace, item);
        }
        entity.setMotion(new Vector3(blockFace.getXOffset(), (float)blockFace.getYOffset() + 0.1f, blockFace.getZOffset()).normalize().multiply(1.3));
        ((EntityProjectile)entity).inaccurate(6.0f);
        ((EntityProjectile)entity).updateRotation();
        entity.spawnToAll();
        return null;
    }
}


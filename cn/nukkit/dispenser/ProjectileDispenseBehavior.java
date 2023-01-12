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
import cn.nukkit.nbt.tag.CompoundTag;

public class ProjectileDispenseBehavior
extends DefaultDispenseBehavior {
    private final String a;

    public ProjectileDispenseBehavior(String string) {
        this.a = string;
    }

    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Vector3 vector3 = blockDispenser.getDispensePosition();
        CompoundTag compoundTag = Entity.getDefaultNBT(vector3);
        this.correctNBT(compoundTag, item);
        Entity entity = Entity.createEntity(this.a, (FullChunk)blockDispenser.level.getChunk(vector3.getChunkX(), vector3.getChunkZ()), compoundTag, new Object[0]);
        if (!(entity instanceof EntityProjectile)) {
            return super.dispense(blockDispenser, blockFace, item);
        }
        Vector3 vector32 = new Vector3(blockFace.getXOffset(), (float)blockFace.getYOffset() + 0.1f, blockFace.getZOffset()).normalize();
        entity.setMotion(vector32);
        ((EntityProjectile)entity).inaccurate(this.getAccuracy());
        entity.setMotion(entity.getMotion().multiply(this.getMotion()));
        ((EntityProjectile)entity).updateRotation();
        entity.spawnToAll();
        return null;
    }

    protected double getMotion() {
        return 1.1;
    }

    protected float getAccuracy() {
        return 6.0f;
    }

    protected void correctNBT(CompoundTag compoundTag) {
        this.correctNBT(compoundTag, null);
    }

    protected void correctNBT(CompoundTag compoundTag, Item item) {
        if (item != null) {
            if (item.getId() == 438 || item.getId() == 441) {
                compoundTag.putInt("PotionId", item.getDamage());
            } else if (item.getId() == 262 && item.getDamage() > 0) {
                compoundTag.putByte("arrowData", item.getDamage());
            }
        }
    }
}


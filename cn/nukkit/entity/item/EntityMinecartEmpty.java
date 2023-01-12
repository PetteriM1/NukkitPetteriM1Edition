/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.item.EntityMinecartAbstract;
import cn.nukkit.entity.passive.EntityWaterAnimal;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.MinecartType;

public class EntityMinecartEmpty
extends EntityMinecartAbstract {
    public static final int NETWORK_ID = 84;

    @Override
    public int getNetworkId() {
        return 84;
    }

    public EntityMinecartEmpty(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
        this.setName("Minecart");
    }

    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(0);
    }

    @Override
    public boolean isRideable() {
        return true;
    }

    @Override
    protected void activate(int n, int n2, int n3, boolean bl) {
        if (bl && this.riding != null) {
            this.mountEntity(this.riding);
        }
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.timing != null) {
            this.timing.startTiming();
        }
        boolean bl = super.onUpdate(n);
        if (this.passengers.isEmpty()) {
            for (Entity entity : this.level.getCollidingEntities(this.boundingBox.grow(0.2f, 0.0, 0.2f), this)) {
                if (entity.riding != null || !(entity instanceof EntityLiving) || entity instanceof Player || entity instanceof EntityWaterAnimal) continue;
                this.mountEntity(entity);
                bl = true;
                break;
            }
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl;
    }

    @Override
    public String getInteractButtonText() {
        return this.passengers.isEmpty() ? "action.interact.ride.minecart" : "";
    }
}


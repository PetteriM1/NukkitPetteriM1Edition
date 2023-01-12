/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityWalking;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public abstract class EntityWalkingAnimal
extends EntityWalking
implements EntityAnimal {
    private int t = 0;

    public EntityWalkingAnimal(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (!this.isAlive()) {
            if (++this.deadTicks >= 23) {
                this.close();
                return false;
            }
            return true;
        }
        if (this.t > 0) {
            --this.t;
            if (this.t == 0) {
                this.doPanic(false);
            }
        }
        int n2 = n - this.lastUpdate;
        this.lastUpdate = n;
        this.entityBaseTick(n2);
        Vector3 vector3 = this.updateMove(n2);
        return true;
    }

    public int getPanicTicks() {
        return this.t;
    }

    public void doPanic(boolean bl) {
        if (bl) {
            int n;
            this.t = n = Utils.rand(60, 100);
            this.stayTime = 0;
            this.moveTime = n;
            this.moveMultiplier = 1.8f;
        } else {
            this.moveMultiplier = 1.0f;
        }
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled() && entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
            this.doPanic(true);
        }
        return true;
    }

    @Override
    public boolean canTarget(Entity entity) {
        return (this.isInLove() || entity instanceof Player) && entity.canBeFollowed();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.vehicle.VehicleDamageEvent;
import cn.nukkit.event.vehicle.VehicleDestroyEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityVehicle
extends Entity
implements EntityRideable,
EntityInteractable {
    private int k;
    private int m;
    private int l;
    protected boolean rollingDirection = true;

    public EntityVehicle(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    public int getRollingAmplitude() {
        return this.k;
    }

    public void setRollingAmplitude(int n) {
        this.k = n;
        this.setDataProperty(new IntEntityData(11, n));
    }

    public int getRollingDirection() {
        return this.m;
    }

    public void setRollingDirection(int n) {
        this.m = n;
        this.setDataProperty(new IntEntityData(12, n));
    }

    public int getDamage() {
        return this.l;
    }

    public void setDamage(int n) {
        this.l = n;
        this.setDataProperty(new IntEntityData(1, n));
    }

    @Override
    public String getInteractButtonText() {
        return "action.interact.mount";
    }

    @Override
    public boolean canDoInteraction() {
        return this.passengers.isEmpty();
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.y < -16.0) {
            this.close();
        }
        if (this.closed) {
            return false;
        }
        if (this.getRollingAmplitude() > 0) {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }
        this.updateMovement();
        return true;
    }

    protected boolean performHurtAnimation() {
        this.setRollingAmplitude(9);
        this.setRollingDirection(this.rollingDirection ? 1 : -1);
        this.rollingDirection = !this.rollingDirection;
        return true;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        Object object;
        VehicleDamageEvent vehicleDamageEvent = new VehicleDamageEvent(this, entityDamageEvent.getEntity(), entityDamageEvent.getFinalDamage());
        this.getServer().getPluginManager().callEvent(vehicleDamageEvent);
        if (vehicleDamageEvent.isCancelled()) {
            return false;
        }
        boolean bl = false;
        if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
            object = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager();
            boolean bl2 = bl = object instanceof Player && ((Player)object).isCreative();
        }
        if (bl || this.getHealth() - entityDamageEvent.getFinalDamage() < 1.0f) {
            object = new VehicleDestroyEvent(this, entityDamageEvent.getEntity());
            this.getServer().getPluginManager().callEvent((Event)object);
            if (((Event)object).isCancelled()) {
                return false;
            }
        }
        if (bl) {
            entityDamageEvent.setDamage(1000.0f);
        }
        return super.attack(entityDamageEvent);
    }
}


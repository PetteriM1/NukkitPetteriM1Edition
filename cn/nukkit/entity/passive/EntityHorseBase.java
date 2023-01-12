/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityControllable;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.entity.passive.EntityDonkey;
import cn.nukkit.entity.passive.EntityLlama;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class EntityHorseBase
extends EntityWalkingAnimal
implements EntityRideable,
EntityControllable {
    private boolean v;
    private short u;

    public EntityHorseBase(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return -1;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        if (this.namedTag.contains("Saddle")) {
            this.setSaddled(this.namedTag.getBoolean("Saddle"));
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Saddle", this.isSaddled());
    }

    @Override
    public boolean mountEntity(Entity entity) {
        Objects.requireNonNull(entity, "The target of the mounting entity can't be null");
        if (entity.riding != null) {
            this.dismountEntity(entity);
            this.motionX = 0.0;
            this.motionZ = 0.0;
            this.stayTime = 20;
        } else {
            if (this.isPassenger(entity)) {
                return false;
            }
            this.broadcastLinkPacket(entity, (byte)1);
            entity.riding = this;
            entity.setDataFlag(0, 2, true);
            entity.setDataProperty(new Vector3fEntityData(56, new Vector3f(0.0f, this instanceof EntityDonkey ? 2.1f : 2.3f, 0.0f)));
            if (!(this instanceof EntityLlama) && entity instanceof Player) {
                this.setDataFlag(0, 45, true);
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.entries = new Attribute[]{Attribute.getAttribute(14)};
                updateAttributesPacket.entityId = this.id;
                ((Player)entity).dataPacket(updateAttributesPacket);
            }
            this.passengers.add(entity);
        }
        return true;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (this.isFeedItem(item) && !this.isInLoveCooldown()) {
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(item.getId(), 0, 1)));
            this.setInLove();
            return true;
        }
        if (this.canBeSaddled() && !this.isSaddled() && item.getId() == 329) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 36);
            this.setSaddled(true);
        } else if (!(!this.passengers.isEmpty() || this.isBaby() || player.isSneaking() || this.canBeSaddled() && !this.isSaddled() || player.riding != null)) {
            this.mountEntity(player);
        }
        return super.onInteract(player, item, vector3);
    }

    public boolean canBeSaddled() {
        return !this.isBaby();
    }

    public boolean isSaddled() {
        return this.v;
    }

    public void setSaddled(boolean bl) {
        if (this.canBeSaddled()) {
            this.v = bl;
            this.setDataFlag(0, 8, bl);
        }
    }

    public boolean isFeedItem(Item item) {
        return item.getId() == 296 || item.getId() == 260 || item.getId() == 170 || item.getId() == 322 || item.getId() == 353 || item.getId() == 297 || item.getId() == 396;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.u > 0) {
            this.u = (short)(this.u - 1);
            if (this.u == 0) {
                this.setDataFlag(0, 39, false);
            }
        }
        Iterator iterator = this.passengers.iterator();
        while (iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if (entity.isAlive()) continue;
            if (entity.riding == this) {
                entity.riding = null;
            }
            iterator.remove();
        }
        return super.onUpdate(n);
    }

    @Override
    public void onPlayerInput(Player player, double d2, double d3) {
        this.stayTime = 0;
        this.moveTime = 10;
        this.setBothYaw(player.yaw);
        if (d3 < 0.0) {
            d3 /= 2.0;
        }
        double d4 = (d2 *= 0.4) * d2 + d3 * d3;
        double d5 = 0.6;
        if (d4 >= 1.0E-4) {
            if ((d4 = Math.sqrt(d4)) < 1.0) {
                d4 = 1.0;
            }
            d4 = d5 / d4;
            double d6 = Math.sin(this.yaw * 0.017453292);
            double d7 = Math.cos(this.yaw * 0.017453292);
            this.motionX = (d2 *= d4) * d7 - (d3 *= d4) * d6;
            this.motionZ = d3 * d7 + d2 * d6;
            if (this.u > 0) {
                this.motionX *= 0.3;
                this.motionZ *= 0.3;
            }
        } else {
            this.motionX = 0.0;
            this.motionZ = 0.0;
        }
    }

    @Override
    public void onJump(Player player, int n) {
        if (this.horseOnGround) {
            this.setDataFlag(0, 39, true);
            if (n > 10) {
                n = 8;
            }
            this.motionY = (double)n * this.getHorseJumpSpeed();
            this.u = (short)20;
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return this.passengers.isEmpty();
    }

    @Override
    public boolean canDespawn() {
        if (this.isSaddled()) {
            return false;
        }
        return super.canDespawn();
    }

    @Override
    public void updatePassengers() {
        if (this.passengers.isEmpty()) {
            return;
        }
        for (Entity entity : new ArrayList(this.passengers)) {
            if (!entity.isAlive() || this.getNetworkId() != 26 && this.isInsideOfWater()) {
                this.dismountEntity(entity);
                continue;
            }
            this.updatePassengerPosition(entity);
        }
    }

    public double getHorseJumpSpeed() {
        return 0.05;
    }
}


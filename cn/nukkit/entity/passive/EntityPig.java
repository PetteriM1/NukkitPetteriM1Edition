/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityControllable;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class EntityPig
extends EntityWalkingAnimal
implements EntityRideable,
EntityControllable {
    public static final int NETWORK_ID = 12;
    private boolean u;

    public EntityPig(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 12;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(10);
        super.initEntity();
        if (this.namedTag.contains("Saddle")) {
            this.setSaddled(this.namedTag.getBoolean("Saddle"));
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            int n = player.getInventory().getItemInHandFast().getId();
            return player.spawned && player.isAlive() && !player.closed && (n == 391 || n == 392 || n == 398 || n == 457) && d2 <= 40.0;
        }
        return super.targetOption(entityCreature, d2);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 391 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(391)));
            this.setInLove();
            return true;
        }
        if (item.getId() == 392 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(392)));
            this.setInLove();
            return true;
        }
        if (item.getId() == 457 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(457)));
            this.setInLove();
            return true;
        }
        if (item.getId() == 329 && !this.isSaddled() && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 36);
            this.setSaddled(true);
        } else if (this.isSaddled() && this.passengers.isEmpty() && !this.isBaby() && !player.isSneaking() && player.riding == null) {
            this.mountEntity(player);
        }
        return super.onInteract(player, item, vector3);
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            for (int k = 0; k < Utils.rand(1, 3); ++k) {
                arrayList.add(Item.get(this.isOnFire() ? 320 : 319, 0, 1));
            }
        }
        if (this.isSaddled()) {
            arrayList.add(Item.get(329, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public boolean mountEntity(Entity entity, byte by) {
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
            entity.setDataProperty(new Vector3fEntityData(56, new Vector3f(0.0f, 1.85001f, 0.0f)));
            entity.setDataProperty(new FloatEntityData(58, 181.0f));
            this.passengers.add(entity);
        }
        return true;
    }

    @Override
    public boolean onUpdate(int n) {
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
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Saddle", this.isSaddled());
    }

    public boolean isSaddled() {
        return this.u;
    }

    public void setSaddled(boolean bl) {
        this.u = bl;
        this.setDataFlag(0, 8, bl);
    }

    @Override
    public void onPlayerInput(Player player, double d2, double d3) {
        if (player.getInventory().getItemInHandFast().getId() == 398) {
            this.stayTime = 0;
            this.moveTime = 10;
            this.setBothYaw(player.yaw);
            double d4 = (d2 *= 0.4) * d2 + d3 * d3;
            double d5 = 0.3;
            if (d4 >= 1.0E-4) {
                if ((d4 = Math.sqrt(d4)) < 1.0) {
                    d4 = 1.0;
                }
                d4 = d5 / d4;
                double d6 = Math.sin(this.yaw * 0.017453292);
                double d7 = Math.cos(this.yaw * 0.017453292);
                this.motionX = (d2 *= d4) * d7 - (d3 *= d4) * d6;
                this.motionZ = d3 * d7 + d2 * d6;
            } else {
                this.motionX = 0.0;
                this.motionZ = 0.0;
            }
        }
    }

    @Override
    protected void checkTarget() {
        if (this.passengers.isEmpty() || !(this.getPassengers().get(0) instanceof Player) || ((Player)this.getPassengers().get(0)).getInventory().getItemInHandFast().getId() != 398) {
            super.checkTarget();
        }
    }

    @Override
    public boolean canDespawn() {
        if (this.isSaddled()) {
            return false;
        }
        return super.canDespawn();
    }

    @Override
    public void onStruckByLightning(Entity entity) {
        Entity entity2 = Entity.createEntity("ZombiePigman", (Position)this, new Object[0]);
        if (entity2 != null) {
            CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(36, this, entity2.namedTag, CreatureSpawnEvent.SpawnReason.LIGHTNING);
            this.getServer().getPluginManager().callEvent(creatureSpawnEvent);
            if (creatureSpawnEvent.isCancelled()) {
                entity2.close();
                return;
            }
            entity2.yaw = this.yaw;
            entity2.pitch = this.pitch;
            entity2.setImmobile(this.isImmobile());
            if (this.hasCustomName()) {
                entity2.setNameTag(this.getNameTag());
                entity2.setNameTagVisible(this.isNameTagVisible());
                entity2.setNameTagAlwaysVisible(this.isNameTagAlwaysVisible());
            }
            this.close();
            entity2.spawnToAll();
        } else {
            super.onStruckByLightning(entity);
        }
    }

    @Override
    public void updatePassengers() {
        if (this.passengers.isEmpty()) {
            return;
        }
        for (Entity entity : new ArrayList(this.passengers)) {
            if (!entity.isAlive() || this.isInsideOfWater()) {
                this.dismountEntity(entity);
                continue;
            }
            this.updatePassengerPosition(entity);
        }
    }
}


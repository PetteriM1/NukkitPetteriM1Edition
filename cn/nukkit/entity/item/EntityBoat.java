/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.entity.item.a;
import cn.nukkit.entity.passive.EntityWaterAnimal;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.vehicle.VehicleMoveEvent;
import cn.nukkit.event.vehicle.VehicleUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AnimatePacket;
import java.util.ArrayList;

public class EntityBoat
extends EntityVehicle {
    public static final int NETWORK_ID = 90;
    public static final Vector3f RIDER_PLAYER_OFFSET = new Vector3f(0.0f, 1.02001f, 0.0f);
    public static final Vector3f RIDER_OFFSET = new Vector3f(0.0f, -0.2f, 0.0f);
    public static final Vector3f PASSENGER_OFFSET = new Vector3f(-0.6f);
    public static final Vector3f RIDER_PASSENGER_OFFSET = new Vector3f(0.2f);
    public static final int RIDER_INDEX = 0;
    public static final int PASSENGER_INDEX = 1;
    public static final double SINKING_DEPTH = 0.07;
    public static final double SINKING_SPEED = 5.0E-4;
    public static final double SINKING_MAX_SPEED = 0.005;
    protected boolean sinking = true;
    public int woodID;

    public EntityBoat(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(40);
        super.initEntity();
        this.setHealth(40.0f);
        if (this.namedTag.contains("Variant")) {
            this.woodID = this.namedTag.getInt("Variant");
        }
        this.dataProperties.putInt(2, this.woodID);
        this.dataProperties.putBoolean(119, true);
        this.dataProperties.putString(121, "{\"apply_gravity\":true,\"base_buoyancy\":1.0,\"big_wave_probability\":0.02999999932944775,\"big_wave_speed\":10.0,\"drag_down_on_buoyancy_removed\":0.0,\"liquid_blocks\":[\"minecraft:water\",\"minecraft:flowing_water\"],\"simulate_waves\":true}");
    }

    @Override
    public float getHeight() {
        return 0.455f;
    }

    @Override
    public float getWidth() {
        return 1.4f;
    }

    @Override
    protected float getDrag() {
        return 0.1f;
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    public float getBaseOffset() {
        return 0.375f;
    }

    @Override
    public int getNetworkId() {
        return 90;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (this.invulnerable) {
            return false;
        }
        entityDamageEvent.setDamage(entityDamageEvent.getDamage() * 2.0f);
        boolean bl = super.attack(entityDamageEvent);
        if (this.isAlive()) {
            this.performHurtAnimation();
        }
        return bl;
    }

    @Override
    public void close() {
        super.close();
        if (!this.passengers.isEmpty()) {
            for (Entity entity : new ArrayList(this.passengers)) {
                this.dismountEntity(entity);
                entity.riding = null;
            }
        }
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        int n2 = n - this.lastUpdate;
        if (n2 <= 0 && !this.justCreated) {
            return true;
        }
        this.lastUpdate = n;
        boolean bl = this.entityBaseTick(n2);
        if (this.isAlive()) {
            super.onUpdate(n);
            double d2 = this.getWaterLevel();
            if (!this.hasControllingPassenger()) {
                if (d2 > 0.07 && !this.sinking) {
                    this.sinking = true;
                } else if (d2 < -0.07 && this.sinking) {
                    this.sinking = false;
                }
                if (d2 < -0.07) {
                    this.motionY = Math.min(0.05, this.motionY + 0.005);
                } else if (d2 < 0.0 || !this.sinking) {
                    double d3 = this.motionY = this.motionY > 0.005 ? Math.max(this.motionY - 0.02, 0.005) : this.motionY + 5.0E-4;
                }
            }
            if (this.checkObstruction(this.x, this.y, this.z)) {
                bl = true;
            }
            double d4 = 1.0f - this.getDrag();
            if (this.onGround && (Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5)) {
                d4 *= this.getLevel().getBlock(this.chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ(), false).getFrictionFactor();
            }
            this.motionX *= d4;
            if (!this.hasControllingPassenger() && (d2 > 0.07 || this.sinking)) {
                this.motionY = d2 > 0.5 ? this.motionY - (double)this.getGravity() : (this.motionY - 5.0E-4 < -0.005 ? this.motionY : this.motionY - 5.0E-4);
            }
            this.motionZ *= d4;
            Location location = new Location(this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch, this.level);
            Location location2 = new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.level);
            if (!this.getServer().suomiCraftPEMode()) {
                this.getServer().getPluginManager().callEvent(new VehicleUpdateEvent(this));
            }
            if (!location.equals(location2)) {
                this.getServer().getPluginManager().callEvent(new VehicleMoveEvent(this, location, location2));
            }
            this.move(this.motionX, this.motionY, this.motionZ);
            this.updateMovement();
            if (this.age % 5 == 0) {
                Position[] positionArray;
                int n3 = this.passengers.size();
                if (n3 > 0) {
                    positionArray = this.level.getCollisionBlocks(this.getBoundingBox().grow(0.1, 0.3, 0.1));
                    for (Position position : positionArray) {
                        if (((Block)position).getId() != 111) continue;
                        this.level.setBlockAt((int)((Block)position).x, (int)((Block)position).y, (int)((Block)position).z, 0, 0);
                        this.level.dropItem(position, Item.get(111, 0, 1));
                    }
                }
                if (n3 < 2) {
                    positionArray = this.level.getCollidingEntities(this.boundingBox.grow(0.2f, 0.0, 0.2f), this);
                    for (Position position : positionArray) {
                        boolean bl2 = this.isPassenger((Entity)position);
                        if (position instanceof Player && !bl2) {
                            ((Entity)position).resetFallDistance();
                        }
                        if (((Entity)position).riding != null || !(position instanceof EntityLiving) || position instanceof Player || position instanceof EntityWaterAnimal || bl2) continue;
                        this.mountEntity((Entity)position);
                        if (this.isFull()) break;
                    }
                }
            }
        }
        return bl || !this.onGround || Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionY) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5;
    }

    @Override
    public void updatePassengers() {
        this.updatePassengers(false);
    }

    public void updatePassengers(boolean bl) {
        Object object;
        if (this.passengers.isEmpty()) {
            return;
        }
        for (Object object2 : new ArrayList(this.passengers)) {
            if (((Entity)object2).isAlive()) continue;
            this.dismountEntity((Entity)object2);
        }
        if (this.passengers.size() == 1) {
            object = (Entity)this.passengers.get(0);
            ((Entity)object).setSeatPosition(this.getMountedOffset((Entity)object));
            super.updatePassengerPosition((Entity)object);
            if (bl) {
                this.broadcastLinkPacket((Entity)object, (byte)1);
            }
        } else if (this.passengers.size() == 2) {
            Object object2;
            object = (Entity)this.passengers.get(0);
            if (!(object instanceof Player) && (object2 = (Entity)this.passengers.get(1)) instanceof Player) {
                this.passengers.set(0, object2);
                this.passengers.set(1, object);
                object = object2;
            }
            ((Entity)object).setSeatPosition(this.getMountedOffset((Entity)object).add(RIDER_PASSENGER_OFFSET));
            super.updatePassengerPosition((Entity)object);
            if (bl) {
                this.broadcastLinkPacket((Entity)object, (byte)1);
            }
            object = (Entity)this.passengers.get(1);
            ((Entity)object).setSeatPosition(this.getMountedOffset((Entity)object).add(PASSENGER_OFFSET));
            super.updatePassengerPosition((Entity)object);
            if (bl) {
                this.broadcastLinkPacket((Entity)object, (byte)2);
            }
            float f2 = ((Entity)object).getId() % 2L == 0L ? 90.0f : 270.0f;
            ((Entity)object).setRotation(this.yaw + (double)f2, ((Entity)object).pitch);
            ((Entity)object).updateMovement();
        } else {
            for (Entity entity : this.passengers) {
                super.updatePassengerPosition(entity);
            }
        }
    }

    public double getWaterLevel() {
        double d2 = this.boundingBox.minY + (double)this.getBaseOffset();
        a a2 = new a(this, d2);
        this.boundingBox.forEach(a2);
        return (Double)a2.get();
    }

    @Override
    public boolean mountEntity(Entity entity) {
        boolean bl = this.passengers.size() >= 1 && this.passengers.get(0) instanceof Player;
        byte by = 2;
        if (!bl && (entity instanceof Player || this.passengers.isEmpty())) {
            by = 1;
        }
        boolean bl2 = super.mountEntity(entity, by);
        if (entity.riding == this) {
            this.updatePassengers(true);
            entity.setDataProperty(new ByteEntityData(57, 1), !(entity instanceof Player));
            if (entity instanceof Player) {
                entity.setDataProperty(new FloatEntityData(58, 90.0f), false);
                entity.setDataProperty(new FloatEntityData(59, 1.0f), false);
                if (((Player)entity).protocol >= 428) {
                    entity.setDataProperty(new FloatEntityData(60, -90.0f), false);
                }
                entity.sendData((Player)entity);
            }
        }
        return bl2;
    }

    @Override
    protected void updatePassengerPosition(Entity entity) {
        this.updatePassengers();
    }

    @Override
    public boolean dismountEntity(Entity entity) {
        boolean bl = super.dismountEntity(entity);
        if (bl) {
            this.updatePassengers();
            if (entity instanceof Player) {
                entity.setDataPropertyAndSendOnlyToSelf(new ByteEntityData(57, 0));
            } else {
                entity.setDataProperty(new ByteEntityData(57, 0), true);
            }
        }
        return bl;
    }

    @Override
    public boolean isControlling(Entity entity) {
        return entity instanceof Player && this.passengers.indexOf(entity) == 0;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (this.isFull() || this.getWaterLevel() < -0.07) {
            return false;
        }
        this.mountEntity(player);
        return super.onInteract(player, item, vector3);
    }

    @Override
    public Vector3f getMountedOffset(Entity entity) {
        return entity instanceof Player ? RIDER_PLAYER_OFFSET : RIDER_OFFSET;
    }

    public void onPaddle(AnimatePacket.Action action, float f2) {
        int n;
        int n2 = n = action == AnimatePacket.Action.ROW_RIGHT ? 14 : 13;
        if (this.getDataPropertyFloat(n) != f2) {
            this.setDataProperty(new FloatEntityData(n, f2));
        }
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        if (this.riding == null && entity.riding != this && !entity.passengers.contains(this)) {
            if (!entity.boundingBox.intersectsWith(this.boundingBox.grow(0.2f, -0.1, 0.2f)) || entity instanceof Player && ((Player)entity).getGamemode() == 3) {
                return;
            }
            double d2 = entity.x - this.x;
            double d3 = entity.z - this.z;
            double d4 = NukkitMath.getDirection(d2, d3);
            if (d4 >= (double)0.01f) {
                d4 = Math.sqrt(d4);
                d2 /= d4;
                d3 /= d4;
                double d5 = Math.min(1.0 / d4, 1.0);
                d2 *= d5;
                d3 *= d5;
                d2 *= (double)0.05f;
                d3 *= (double)0.05f;
                d2 *= 1.0 + this.entityCollisionReduction;
                d3 *= 1.0 + this.entityCollisionReduction;
                if (this.riding == null) {
                    this.motionX -= d2;
                    this.motionZ -= d3;
                }
            }
        }
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    @Override
    public void kill() {
        if (!this.isAlive()) {
            return;
        }
        super.kill();
        if (this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
            Entity entity;
            if (this.lastDamageCause instanceof EntityDamageByEntityEvent && (entity = ((EntityDamageByEntityEvent)this.lastDamageCause).getDamager()) instanceof Player && ((Player)entity).isCreative()) {
                return;
            }
            this.dropItem();
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Variant", this.woodID);
    }

    public int getVariant() {
        return this.woodID;
    }

    public void setVariant(int n) {
        this.woodID = n;
        this.dataProperties.putInt(2, n);
    }

    @Override
    public boolean goToNewChunk(FullChunk fullChunk) {
        if (fullChunk.getEntities().size() > 200) {
            if (!this.isClosed() && this.isAlive() && this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
                this.dropItem();
            }
            this.close();
            return false;
        }
        return true;
    }

    public void onInput(double d2, double d3, double d4, double d5) {
        this.setPositionAndRotation(this.temporalVector.setComponents(d2, d3 - (double)this.getBaseOffset(), d4), d5 % 360.0, 0.0);
    }

    public boolean isFull() {
        return this.passengers.size() >= 2;
    }

    @Override
    public String getInteractButtonText() {
        return !this.isFull() ? "action.interact.ride.boat" : "";
    }

    protected void dropItem() {
        this.level.dropItem(this, Item.get(333, this.woodID));
    }
}


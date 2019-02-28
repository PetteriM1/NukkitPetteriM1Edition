package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.vehicle.VehicleDamageEvent;
import cn.nukkit.event.vehicle.VehicleDestroyEvent;
import cn.nukkit.event.vehicle.VehicleMoveEvent;
import cn.nukkit.event.vehicle.VehicleUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBoat;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created by yescallop on 2016/2/13.
 */
public class EntityBoat extends EntityVehicle {

    public static final int NETWORK_ID = 90;

    public EntityBoat(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public boolean isRideable() {
        return true;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.setHealth(4);
        this.setMaxHealth(4);
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public float getWidth() {
        return 1.6f;
    }

    @Override
    protected float getDrag() {
        return 0.1f;
    }

    @Override
    public float getBaseOffset() {
        return 0.35f;
    }

    @Override
    public float getMountedYOffset() {
        return 0.71f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (invulnerable || source.isCancelled()) {
            return false;
        } else {
            VehicleDamageEvent event = new VehicleDamageEvent(this, source.getEntity(), source.getFinalDamage());
            this.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }

            if (source.getCause() == EntityDamageEvent.DamageCause.MAGMA) {
                event.setCancelled(true);
                return false;
            }

            this.performHurtAnimation((int) event.getDamage());

            boolean instantKill = true;
            boolean onCreative = false;

            if (source instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
                onCreative = damager instanceof Player && ((Player) damager).isCreative();
            }

            if (instantKill) {
                VehicleDestroyEvent event2 = new VehicleDestroyEvent(this, source.getEntity());
                this.getServer().getPluginManager().callEvent(event2);

                if (event2.isCancelled()) {
                    return false;
                }

                if (linkedEntity != null) {
                    this.mountEntity(linkedEntity);
                }

                if (!onCreative && level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
                    this.level.dropItem(this, new ItemBoat());
                }

                this.close();
            }
        }

        return true;
    }

    @Override
    public void close() {
        super.close();

        if (this.linkedEntity instanceof Player) {
            this.linkedEntity.riding = null;
        }

        SmokeParticle particle = new SmokeParticle(this);
        this.level.addParticle(particle);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }

        this.timing.startTiming();

        this.lastUpdate = currentTick;

        this.entityBaseTick(tickDiff);

        if (this.isAlive()) {

            this.checkObstruction(this.x, this.y, this.z);

            if (this.isInsideOfWater() && this.linkedEntity == null) {
                this.motionY = 0.01;
            } else if (this.level.getBlock(new Vector3(this.x, this.y, this.z)).getBoundingBox() != null || this.isInsideOfWater() || this.isInsideOfSolid()) {
                this.motionY = 0;
            } else if (!this.isOnGround() && !this.isInsideOfWater()) {
                this.motionY =- 0.2;
            }

            this.move(this.motionX, this.motionY, this.motionZ);

            if (this.linkedEntity == null) {
                double friction = 1 - this.getDrag();

                if (this.onGround && (Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionZ) > 0.00001)) {
                    friction *= this.getLevel().getBlock(this.temporalVector.setComponents((int) Math.floor(this.x), (int) Math.floor(this.y - 1), (int) Math.floor(this.z) - 1)).getFrictionFactor();
                }

                this.motionX *= friction;
                this.motionZ *= friction;
            }

            Location from = new Location(lastX, lastY, lastZ, lastYaw, lastPitch, level);
            Location to = new Location(this.x, this.y, this.z, this.yaw, this.pitch, level);

            this.getServer().getPluginManager().callEvent(new VehicleUpdateEvent(this));

            if (!from.equals(to)) {
                this.getServer().getPluginManager().callEvent(new VehicleMoveEvent(this, from, to));
            }

            this.updateMovement();
        }

        this.timing.stopTiming();

        return true;
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (this.linkedEntity != null) {
            return false;
        }

        super.mountEntity(player);
        return true;
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        if (entity.riding != this && entity.linkedEntity != this && entity.y - this.y < 0.5) {
            double dx = entity.x - this.x;
            double dy = entity.z - this.z;
            double dz = NukkitMath.getDirection(dx, dy);

            if (dz >= 0.009999999776482582D) {
                dz = MathHelper.sqrt((float) dz);
                dx /= dz;
                dy /= dz;
                double d3 = 1.0D / dz;

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                dx *= d3;
                dy *= d3;
                dx *= 0.05000000074505806D;
                dy *= 0.05000000074505806D;
                dx *= 1.0F + entityCollisionReduction;
                dz *= 1.0F + entityCollisionReduction;
                if (this.riding == null) {
                    motionX -= dx;
                    motionZ -= dy;
                }
            }
        }
    }
}

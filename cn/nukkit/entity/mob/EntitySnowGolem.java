/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntitySnowGolem
extends EntityWalkingMob {
    public static final int NETWORK_ID = 21;
    private boolean B = false;
    private boolean A;
    private int w;

    public EntitySnowGolem(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 21;
    }

    @Override
    public float getWidth() {
        return 0.7f;
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }

    @Override
    public void initEntity() {
        this.setFriendly(true);
        this.setMaxHealth(4);
        super.initEntity();
        this.noFallDamage = true;
        if (this.namedTag.getBoolean("Sheared")) {
            this.shear(true);
        }
        this.A = this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING);
        if (this.A) {
            this.w = this.chunk.getBiomeId(this.getFloorX() & 0xF, this.getFloorZ() & 0xF);
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return (!(entityCreature instanceof Player) || entityCreature.getId() == this.isAngryTo) && entityCreature.isAlive() && d2 <= 100.0;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && Utils.rand(1, 32) < 4 && this.distanceSquared(entity) <= 55.0) {
            this.attackDelay = 0;
            double d2 = 1.2;
            double d3 = this.yaw + Utils.rand(-4.0, 4.0);
            double d4 = this.pitch + Utils.rand(-4.0, 4.0);
            Location location = new Location(this.x + -Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * 0.5, this.y + (double)this.getEyeHeight(), this.z + Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * 0.5, d3, d4, this.level);
            Entity entity2 = Entity.createEntity("Snowball", (Position)location, this);
            if (entity2 == null) {
                return;
            }
            EntitySnowball entitySnowball = (EntitySnowball)entity2;
            entitySnowball.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * d2 * d2, -Math.sin(FastMathLite.toRadians(d4)) * d2 * d2, Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * d2 * d2).multiply(d2));
            EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(this, Item.get(262, 0, 1), entitySnowball, d2);
            this.server.getPluginManager().callEvent(entityShootBowEvent);
            EntityProjectile entityProjectile = entityShootBowEvent.getProjectile();
            if (entityShootBowEvent.isCancelled()) {
                entityProjectile.close();
            } else if (entityProjectile != null) {
                ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityProjectile);
                this.server.getPluginManager().callEvent(projectileLaunchEvent);
                if (projectileLaunchEvent.isCancelled()) {
                    entityProjectile.close();
                } else {
                    entityProjectile.spawnToAll();
                }
            }
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(332, 0, Utils.rand(0, 15))};
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Snow Golem";
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.age % 20 == 0 && (this.level.getDimension() == 1 || this.level.isRaining() && this.canSeeSky())) {
            this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.FIRE_TICK, 1.0f));
        }
        boolean bl = super.entityBaseTick(n);
        if (!this.closed && this.A && this.age % 10 == 0) {
            if (this.age % 400 == 0) {
                this.w = this.chunk.getBiomeId(this.getFloorX() & 0xF, this.getFloorZ() & 0xF);
            }
            if (this.w != 2 && this.w != 8 && this.w != 21 && this.w != 22 && this.w != 23 && this.w != 37 && this.w != 38 && this.w != 39 && this.w != 130 && this.w != 149 && this.w != 151 && this.w != 165 && this.w != 166 && this.w != 167 && this.level.getBlockIdAt(this.chunk, this.getFloorX(), this.getFloorY(), this.getFloorZ()) == 0 && !Block.transparent[this.level.getBlockIdAt(this.getFloorX(), this.getFloorY() - 1, this.getFloorZ())]) {
                this.level.setBlockAt(this.getFloorX(), this.getFloorY(), this.getFloorZ(), 78, 0);
            }
        }
        return bl;
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 10;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 359 && !this.B) {
            this.shear(true);
            this.level.addLevelSoundEvent(this, 45);
            player.getInventory().getItemInHand().setDamage(item.getDamage() + 1);
            return true;
        }
        return super.onInteract(player, item, vector3);
    }

    public void shear(boolean bl) {
        this.B = bl;
        this.setDataFlag(0, 31, bl);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Sheared", this.isSheared());
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (super.attack(entityDamageEvent)) {
            if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
                this.isAngryTo = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager().getId();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canTarget(Entity entity) {
        return entity.canBeFollowed() && entity.getId() == this.isAngryTo;
    }

    public boolean isSheared() {
        return this.B;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.weather.EntityLightningStrike;
import cn.nukkit.event.entity.CreeperPowerEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityCreeper
extends EntityWalkingMob
implements EntityExplosive {
    public static final int NETWORK_ID = 33;
    private short A = 0;
    private boolean w;

    public EntityCreeper(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 33;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.7f;
    }

    @Override
    public double getSpeed() {
        return 0.9;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        if (this.namedTag.contains("powered")) {
            this.setPowered(this.namedTag.getBoolean("powered"));
        }
    }

    @Override
    public void explode() {
        if (this.closed) {
            return;
        }
        EntityExplosionPrimeEvent entityExplosionPrimeEvent = new EntityExplosionPrimeEvent(this, this.isPowered() ? 6.0 : 3.0);
        this.server.getPluginManager().callEvent(entityExplosionPrimeEvent);
        if (!entityExplosionPrimeEvent.isCancelled()) {
            Explosion explosion = new Explosion(this, (float)entityExplosionPrimeEvent.getForce(), this);
            if (entityExplosionPrimeEvent.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }
            explosion.explodeB();
        }
        this.close();
    }

    @Override
    public boolean onUpdate(int n) {
        double d2;
        double d3;
        if (this.server.getDifficulty() < 1) {
            this.close();
            return false;
        }
        if (!this.isAlive()) {
            if (++this.deadTicks >= 23) {
                this.close();
                return false;
            }
            return true;
        }
        int n2 = n - this.lastUpdate;
        this.lastUpdate = n;
        this.entityBaseTick(n2);
        if (!this.isMovement()) {
            return true;
        }
        if (this.isKnockback()) {
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionY -= (double)this.getGravity();
            this.updateMovement();
            return true;
        }
        Vector3 vector3 = this.target;
        this.checkTarget();
        if (this.target instanceof EntityCreature || vector3 != this.target) {
            d3 = this.target.x - this.x;
            d2 = this.target.z - this.z;
            double d4 = Math.abs(d3) + Math.abs(d2);
            double d5 = this.target.distanceSquared(this);
            if (d5 <= 16.0) {
                if (this.target instanceof EntityCreature) {
                    if (!this.w) {
                        if (this.A == 0) {
                            this.getLevel().addLevelEvent(this, 1005);
                            this.setDataFlag(0, 10, true);
                        }
                        this.A = (short)(this.A + n2);
                        if (this.A >= 30) {
                            this.explode();
                            return false;
                        }
                    }
                    if (d5 <= 1.0) {
                        this.stayTime = 10;
                    }
                }
            } else {
                if (!this.w) {
                    this.setDataFlag(0, 10, false);
                    this.A = 0;
                }
                this.motionX = this.getSpeed() * 0.15 * (d3 / d4);
                this.motionZ = this.getSpeed() * 0.15 * (d2 / d4);
            }
            if (this.stayTime <= 0 || Utils.rand()) {
                this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(d3 / d4, d2 / d4)));
            }
        }
        d3 = this.motionX;
        d2 = this.motionZ;
        boolean bl = this.checkJump(d3, d2);
        if (this.stayTime > 0) {
            this.stayTime -= n2;
            this.move(0.0, this.motionY, 0.0);
        } else {
            Vector2 vector2 = new Vector2(this.x + d3, this.z + d2);
            this.move(d3, this.motionY, d2);
            Vector2 vector22 = new Vector2(this.x, this.z);
            if (!(vector2.x == vector22.x && vector2.y == vector22.y || bl)) {
                this.moveTime -= 90;
            }
        }
        if (!bl) {
            if (this.onGround) {
                this.motionY = 0.0;
            } else if (this.motionY > (double)(-this.getGravity() * 4.0f)) {
                int n3 = this.level.getBlockIdAt(this.chunk, NukkitMath.floorDouble(this.x), (int)(this.y + 0.8), NukkitMath.floorDouble(this.z));
                if (!Block.hasWater(n3) && n3 != 10 && n3 != 11) {
                    this.motionY -= (double)this.getGravity();
                }
            } else {
                this.motionY -= (double)this.getGravity();
            }
        }
        this.updateMovement();
        return true;
    }

    @Override
    public void attackEntity(Entity entity) {
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (int k = 0; k < Utils.rand(0, 2); ++k) {
            arrayList.add(Item.get(289, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 259 && !this.w) {
            this.w = true;
            this.level.addLevelSoundEvent(this, 50);
            this.setDataFlag(0, 10, true);
            this.getLevel().addLevelEvent(this, 1005);
            this.level.getServer().getScheduler().scheduleDelayedTask(null, this::explode, 30);
            return true;
        }
        return super.onInteract(player, item, vector3);
    }

    public boolean isPowered() {
        return this.getDataFlag(0, 9);
    }

    public void setPowered(boolean bl) {
        this.setDataFlag(0, 9, bl);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("powered", this.isPowered());
    }

    @Override
    public void onStruckByLightning(Entity entity) {
        if (this.attack(new EntityDamageByEntityEvent(entity, (Entity)this, EntityDamageEvent.DamageCause.LIGHTNING, 5.0f))) {
            if (this.fireTicks < 160) {
                this.setOnFire(8);
            }
            if (entity instanceof EntityLightningStrike) {
                CreeperPowerEvent creeperPowerEvent = new CreeperPowerEvent(this, (EntityLightningStrike)((Object)entity), CreeperPowerEvent.PowerCause.LIGHTNING);
                this.server.getPluginManager().callEvent(creeperPowerEvent);
                if (!creeperPowerEvent.isCancelled()) {
                    this.setPowered(true);
                }
            }
        }
    }
}


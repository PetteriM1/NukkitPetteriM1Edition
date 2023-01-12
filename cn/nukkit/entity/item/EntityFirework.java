/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.data.NBTEntityData;
import cn.nukkit.entity.mob.EntityEnderDragon;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.EntityEventPacket;
import java.util.concurrent.ThreadLocalRandom;

public class EntityFirework
extends Entity {
    public static final int NETWORK_ID = 72;
    private int k;
    private Item l;

    public EntityFirework(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public void initEntity() {
        super.initEntity();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        this.motionX = threadLocalRandom.nextGaussian() * 0.001;
        this.motionZ = threadLocalRandom.nextGaussian() * 0.001;
        this.motionY = 0.05;
        if (this.namedTag.contains("FireworkItem")) {
            this.setFirework(NBTIO.getItemHelper(this.namedTag.getCompound("FireworkItem")));
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        if (this.l != null) {
            this.namedTag.putCompound("FireworkItem", NBTIO.putItemHelper(this.l));
        }
    }

    @Override
    public int getNetworkId() {
        return 72;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.age > this.k) {
            this.close();
            return false;
        }
        int n2 = n - this.lastUpdate;
        if (n2 <= 0 && !this.justCreated) {
            return true;
        }
        this.lastUpdate = n;
        if (this.timing != null) {
            this.timing.startTiming();
        }
        boolean bl = this.entityBaseTick(n2);
        if (this.isAlive()) {
            this.motionX *= 1.15;
            this.motionZ *= 1.15;
            this.motionY += 0.04;
            this.move(this.motionX, this.motionY, this.motionZ);
            this.updateMovement();
            float f2 = (float)Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.yaw = (float)(FastMathLite.atan2(this.motionX, this.motionZ) * 57.29577951308232);
            this.pitch = (float)(FastMathLite.atan2(this.motionY, f2) * 57.29577951308232);
            if (this.age == 0) {
                this.getLevel().addLevelSoundEvent(this, 56);
            }
            if (this.age >= this.k) {
                Tag tag;
                EntityEventPacket entityEventPacket = new EntityEventPacket();
                entityEventPacket.event = 25;
                entityEventPacket.eid = this.getId();
                this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), entityEventPacket);
                this.level.addLevelSoundEvent(this, 58, -1, 72);
                if (this.l != null && (tag = this.l.getNamedTag()) != null && (tag = ((CompoundTag)tag).get("Fireworks")) instanceof CompoundTag && (tag = ((CompoundTag)tag).get("Explosions")) instanceof ListTag && !((ListTag)tag).getAll().isEmpty()) {
                    EntityExplosionPrimeEvent entityExplosionPrimeEvent = new EntityExplosionPrimeEvent(this, 2.0);
                    entityExplosionPrimeEvent.setBlockBreaking(false);
                    this.server.getPluginManager().callEvent(entityExplosionPrimeEvent);
                    if (!entityExplosionPrimeEvent.isCancelled()) {
                        double d2 = 5.0;
                        double d3 = NukkitMath.floorDouble(this.x - d2 - 1.0);
                        double d4 = NukkitMath.ceilDouble(this.x + d2 + 1.0);
                        double d5 = NukkitMath.floorDouble(this.y - d2 - 1.0);
                        double d6 = NukkitMath.ceilDouble(this.y + d2 + 1.0);
                        double d7 = NukkitMath.floorDouble(this.z - d2 - 1.0);
                        double d8 = NukkitMath.ceilDouble(this.z + d2 + 1.0);
                        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d3, d5, d7, d4, d6, d8);
                        Entity[] entityArray = this.level.getNearbyEntities(axisAlignedBB, this);
                        Explosion explosion = new Explosion(this, 0.0, null);
                        for (Entity entity : entityArray) {
                            double d9;
                            if (!(entity instanceof EntityLiving) || entity instanceof EntityEnderDragon || !((d9 = entity.distance(this) / d2) <= 1.0)) continue;
                            Vector3 vector3 = entity.subtract(this).normalize();
                            double d10 = explosion.getSeenPercent(this, entity);
                            double d11 = (1.0 - d9) * d10;
                            int n3 = (int)((d11 * d11 + d11) / 2.0 * 7.0 * entityExplosionPrimeEvent.getForce() + 1.0);
                            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, n3));
                            entity.setMotion(vector3.multiply(d11));
                        }
                    }
                }
                this.kill();
                bl = true;
            }
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl || !this.onGround || Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionY) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        return (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.VOID || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) && super.attack(entityDamageEvent);
    }

    public void setFirework(Item item) {
        this.l = item;
        this.setDataProperty(new NBTEntityData(16, this.l));
        int n = Math.max(1, this.l instanceof ItemFirework ? ((ItemFirework)this.l).getFlight() : 1);
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        this.k = 10 * (n + 1) + threadLocalRandom.nextInt(5) + threadLocalRandom.nextInt(6);
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }
}


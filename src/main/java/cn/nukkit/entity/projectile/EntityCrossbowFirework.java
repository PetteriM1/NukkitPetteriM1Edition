package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.NBTEntityData;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;

import java.util.concurrent.ThreadLocalRandom;

public class EntityCrossbowFirework extends EntityProjectile {

    public static final int NETWORK_ID = 72;

    private int lifetime;
    private Item firework;

    public EntityCrossbowFirework(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityCrossbowFirework(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public void initEntity() {
        super.initEntity();

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        this.lifetime = 30 + rand.nextInt(6) + rand.nextInt(7);

        if (namedTag.contains("FireworkItem")) {
            this.setFirework(NBTIO.getItemHelper(namedTag.getCompound("FireworkItem")));
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        if (this.firework != null) {
            this.namedTag.putCompound("FireworkItem", NBTIO.putItemHelper(this.firework));
        }
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        } else if (this.age > this.lifetime) {
            this.close();
            return false;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }

        this.lastUpdate = currentTick;

        boolean hasUpdate = this.entityBaseTick(tickDiff);

        if (this.isAlive()) {
            if (!this.isCollided) {
                this.motionY -= this.getGravity();
            }

            this.move(this.motionX, this.motionY, this.motionZ);

            this.updateMovement();

            if (this.age == 0) {
                this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_LAUNCH);
            }

            if (this.age >= this.lifetime) {
                EntityEventPacket pk = new EntityEventPacket();
                pk.event = EntityEventPacket.FIREWORK_EXPLOSION;
                pk.eid = this.getId();

                this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), pk);

                level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_LARGE_BLAST, -1, NETWORK_ID);

                Tag nbt = firework.getNamedTag();
                if (nbt != null) {
                    nbt = ((CompoundTag) nbt).get("Fireworks");
                    if (nbt instanceof CompoundTag) {
                        nbt = ((CompoundTag) nbt).get("Explosions");
                        if (nbt instanceof ListTag) {
                            if (((ListTag) nbt).size() != 0) {
                                EntityExplosionPrimeEvent ev = new EntityExplosionPrimeEvent(this, 2.5);
                                ev.setBlockBreaking(false);
                                server.getPluginManager().callEvent(ev);
                                if (!ev.isCancelled()) {
                                    Explosion explosion = new Explosion(this, ev.getForce(), this);
                                    explosion.explodeEntity();
                                }
                            }
                        }
                    }
                }

                this.kill(); // Using close() here would remove the firework before the explosion is displayed

                hasUpdate = true;
            }
        }

        return hasUpdate || !this.onGround || Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionY) > 0.00001 || Math.abs(this.motionZ) > 0.00001;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return (source.getCause() == DamageCause.VOID ||
                source.getCause() == DamageCause.FIRE_TICK ||
                source.getCause() == DamageCause.ENTITY_EXPLOSION ||
                source.getCause() == DamageCause.BLOCK_EXPLOSION)
                && super.attack(source);
    }

    public void setFirework(Item item) {
        this.firework = item;
        this.setDataProperty(new NBTEntityData(Entity.DATA_DISPLAY_ITEM, firework));
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }

    @Override
    public float getGravity() {
        return (float) Utils.rand(-0.01, 0.01);
    }
}

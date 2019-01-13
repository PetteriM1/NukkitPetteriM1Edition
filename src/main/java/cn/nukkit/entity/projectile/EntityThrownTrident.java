package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EntityUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by PetteriM1
 */
public class EntityThrownTrident extends EntityProjectile {

    public static final int NETWORK_ID = 73;

    public static final int DATA_SOURCE_ID = 17;

    public boolean firstTickOnGround = true;

    protected Item trident;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.05f;
    }

    @Override
    public float getLength() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.05f;
    }

    @Override
    public float getGravity() {
        return 0.05f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    protected float gravity = 0.04f;
    protected float drag = 0.01f;

    public EntityThrownTrident(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityThrownTrident(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityThrownTrident(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);
        this.setCritical(critical);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.damage = namedTag.contains("damage") ? namedTag.getDouble("damage") : 8;
        this.trident = namedTag.contains("trident") ? NBTIO.getItemHelper(namedTag.getCompound("trident")) : Item.get(0);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.put("trident", NBTIO.putItemHelper(this.trident));
    }

    public Item getItem() {
        return this.trident != null ? this.trident.clone() : Item.get(0);
    }

    public void setItem(Item item) {
        this.trident = item.clone();
    }

    public void setCritical() {
        this.setCritical(true);
    }

    public void setCritical(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_CRITICAL, value);
    }

    public boolean isCritical() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_CRITICAL);
    }

    @Override
    public int getResultDamage() {
        int base = super.getResultDamage();

        if (this.isCritical()) {
            base += ThreadLocalRandom.current().nextInt(base / 2 + 2);
        }

        return base;
    }

    @Override
    protected double getBaseDamage() {
        return 8;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        boolean hasUpdate = super.onUpdate(currentTick);

        if (this.onGround || this.hadCollision) {
            this.setCritical(false);
            if (firstTickOnGround) {
                this.level.addLevelSoundEvent(178, 1, -1, this);
                this.firstTickOnGround = false;
            }
        }

        if (this.age > 1200) {
            this.close();
            hasUpdate = true;
        }

        this.timing.stopTiming();

        return hasUpdate;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.server.getPluginManager().callEvent(new ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity)));
        float damage = this.getResultDamage();

        EntityDamageEvent ev;
        if (this.shootingEntity == null) {
            ev = new EntityDamageByEntityEvent(this, entity, DamageCause.PROJECTILE, damage);
        } else {
            ev = new EntityDamageByChildEntityEvent(this.shootingEntity, this, entity, DamageCause.PROJECTILE, damage);
        }
        entity.attack(ev);
        this.hadCollision = true;
        this.close();
        EntityThrownTrident newTrident = (EntityThrownTrident) EntityUtils.create("ThrownTrident", this);
        newTrident.setItem(trident);
        newTrident.spawnToAll();
    }
}

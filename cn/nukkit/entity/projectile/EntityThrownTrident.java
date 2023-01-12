/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.weather.LightningStrikeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.NumberTag;

public class EntityThrownTrident
extends EntityProjectile {
    public static final int NETWORK_ID = 73;
    protected Item trident;
    protected int pickupMode;
    private Vector3 n;
    private BlockVector3 l;
    private int o;
    private boolean p;
    private int k;
    private boolean s;
    private int q;
    private static final Vector3 r = new Vector3(0.0, 0.0, 0.0);
    private static final BlockVector3 m = new BlockVector3(0, 0, 0);

    @Override
    public int getNetworkId() {
        return 73;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.35f;
    }

    @Override
    public float getGravity() {
        return 0.04f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    public EntityThrownTrident(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityThrownTrident(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    protected void initEntity() {
        ListTag<NumberTag> listTag;
        super.initEntity();
        int n = this.pickupMode = this.namedTag.contains("pickup") ? this.namedTag.getByte("pickup") : 1;
        if (this.namedTag.contains("Trident")) {
            this.trident = NBTIO.getItemHelper(this.namedTag.getCompound("Trident"));
            this.k = this.trident.getEnchantmentLevel(31);
            this.s = this.trident.hasEnchantment(32);
            this.q = this.trident.getEnchantmentLevel(29);
        } else {
            this.trident = Item.get(0);
            this.k = 0;
            this.s = false;
            this.q = 0;
        }
        if (this.namedTag.contains("CollisionPos")) {
            listTag = this.namedTag.getList("CollisionPos", DoubleTag.class);
            this.n = new Vector3(listTag.get((int)0).data, listTag.get((int)1).data, ((DoubleTag)listTag.get((int)2)).data);
        } else {
            this.n = r.clone();
        }
        if (this.namedTag.contains("StuckToBlockPos")) {
            listTag = this.namedTag.getList("StuckToBlockPos", IntTag.class);
            this.l = new BlockVector3(((IntTag)listTag.get((int)0)).data, ((IntTag)listTag.get((int)1)).data, ((IntTag)listTag.get((int)2)).data);
        } else {
            this.l = m.clone();
        }
        this.o = this.namedTag.contains("favoredSlot") ? this.namedTag.getInt("favoredSlot") : -1;
        this.p = this.namedTag.contains("player") ? this.namedTag.getBoolean("player") : true;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.put("Trident", NBTIO.putItemHelper(this.trident));
        this.namedTag.putByte("pickup", this.pickupMode);
        this.namedTag.putList(new ListTag<DoubleTag>("CollisionPos").add(new DoubleTag("0", this.n.x)).add(new DoubleTag("1", this.n.y)).add(new DoubleTag("2", this.n.z)));
        this.namedTag.putList(new ListTag<IntTag>("StuckToBlockPos").add(new IntTag("0", this.l.x)).add(new IntTag("1", this.l.y)).add(new IntTag("2", this.l.z)));
        this.namedTag.putInt("favoredSlot", this.o);
        this.namedTag.putBoolean("player", this.p);
    }

    public Item getItem() {
        return this.trident != null ? this.trident.clone() : Item.get(0);
    }

    public void setItem(Item item) {
        this.trident = item.clone();
        this.k = this.trident.getEnchantmentLevel(31);
        this.s = this.trident.hasEnchantment(32);
        this.q = this.trident.getEnchantmentLevel(29);
    }

    @Override
    protected double getBaseDamage() {
        return 8.0;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.age > 1200 && this.pickupMode < 1) {
            this.close();
            return false;
        }
        boolean bl = super.onUpdate(n);
        if (this.noClip) {
            if (this.a()) {
                Entity entity = this.shootingEntity;
                Vector3 vector3 = new Vector3(entity.x - this.x, entity.y + (double)entity.getEyeHeight() - this.y, entity.z - this.z);
                this.setPosition(new Vector3(this.x, this.y + vector3.y * 0.015 * (double)this.k, this.z));
                this.setMotion(this.getMotion().multiply(0.95).add(vector3.multiply((double)this.k * 0.05)));
                bl = true;
            } else {
                if (!this.closed && this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
                    this.level.dropItem(this, this.trident);
                }
                this.close();
            }
        }
        return bl;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        if (this.noClip) {
            return;
        }
        this.server.getPluginManager().callEvent(new ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity)));
        float f2 = this.getResultDamage();
        if (this.q > 0 && (entity.isInsideOfWater() || entity.getLevel().isRaining() && entity.canSeeSky())) {
            f2 += 2.5f * (float)this.q;
        }
        EntityDamageByEntityEvent entityDamageByEntityEvent = this.shootingEntity == null ? new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.PROJECTILE, f2, this.knockBack) : new EntityDamageByChildEntityEvent(this.shootingEntity, (Entity)this, entity, EntityDamageEvent.DamageCause.PROJECTILE, f2, this.knockBack);
        entity.attack(entityDamageByEntityEvent);
        this.hadCollision = true;
        this.onHit();
        this.setCollisionPos(this);
        this.setMotion(new Vector3(this.getMotion().getX() * -0.01, this.getMotion().getY() * -0.1, this.getMotion().getZ() * -0.01));
        if (this.s && this.level.isThundering() && this.canSeeSky()) {
            EntityLightning entityLightning = new EntityLightning(this.getChunk(), EntityThrownTrident.getDefaultNBT(this));
            LightningStrikeEvent lightningStrikeEvent = new LightningStrikeEvent(this.level, entityLightning);
            this.server.getPluginManager().callEvent(lightningStrikeEvent);
            if (!lightningStrikeEvent.isCancelled()) {
                entityLightning.spawnToAll();
                this.level.addLevelSoundEvent(this, 184);
            } else {
                entityLightning.setEffect(false);
            }
        }
        if (this.a()) {
            this.getLevel().addLevelSoundEvent(this, 179);
            this.noClip = true;
            this.hadCollision = false;
            this.setRope(true);
        }
    }

    @Override
    public void onHit() {
        this.getLevel().addLevelSoundEvent(this, 178);
    }

    @Override
    public void onHitGround() {
        if (this.noClip) {
            return;
        }
        for (Block block : this.level.getCollisionBlocks(this.getBoundingBox().grow(0.1, 0.1, 0.1))) {
            this.setStuckToBlockPos(new BlockVector3(block.getFloorX(), block.getFloorY(), block.getFloorZ()));
            if (!this.a()) continue;
            this.getLevel().addLevelSoundEvent(this, 179);
            this.noClip = true;
            this.setRope(true);
            return;
        }
    }

    public Vector3 getCollisionPos() {
        return this.n;
    }

    public void setCollisionPos(Vector3 vector3) {
        this.n = vector3;
    }

    public BlockVector3 getStuckToBlockPos() {
        return this.l;
    }

    public void setStuckToBlockPos(BlockVector3 blockVector3) {
        this.l = blockVector3;
    }

    public int getFavoredSlot() {
        return this.o;
    }

    public void setFavoredSlot(int n) {
        this.o = n;
    }

    public boolean shotByPlayer() {
        return this.p;
    }

    public void setShotByPlayer(boolean bl) {
        this.p = bl;
    }

    public void setRope(boolean bl) {
        if (bl) {
            this.setDataProperty(new LongEntityData(5, this.shootingEntity.getId()));
        } else {
            this.setDataProperty(new LongEntityData(5, -1L));
        }
        this.setDataFlag(0, 53, bl);
    }

    private boolean a() {
        if (this.k <= 0) {
            return false;
        }
        if (this.getCollisionPos().equals(r) && this.getStuckToBlockPos().equals(m)) {
            return false;
        }
        Entity entity = this.shootingEntity;
        if (entity != null) {
            return entity instanceof Player && entity.isAlive() && !entity.isClosed() && entity.getLevel().getId() == this.getLevel().getId() && !((Player)entity).isSpectator();
        }
        return false;
    }

    public int getPickupMode() {
        return this.pickupMode;
    }

    public void setPickupMode(int n) {
        this.pickupMode = n;
    }
}


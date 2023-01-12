/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.player.PlayerFishEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookEnchanted;
import cn.nukkit.item.ItemFishingRod;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.randomitem.Fishing;
import cn.nukkit.level.Location;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.BubbleParticle;
import cn.nukkit.level.particle.WaterParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.Utils;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class EntityFishingHook
extends EntityProjectile {
    public static final int NETWORK_ID = 77;
    public int waitChance = 120;
    public int waitTimer = 240;
    public boolean attracted = false;
    public int attractTimer = 0;
    public boolean caught = false;
    public int caughtTimer = 0;
    public boolean canCollide = true;
    public Entity caughtEntity = null;
    private long l = 0L;
    public Vector3 fish = null;
    public Item rod = null;
    private static final int[] k = new int[]{24, 23, 17, 26, 28};

    public EntityFishingHook(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityFishingHook(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        if (this.age > 0) {
            this.close();
        }
    }

    @Override
    public int getNetworkId() {
        return 77;
    }

    @Override
    public float getWidth() {
        return 0.2f;
    }

    @Override
    public float getLength() {
        return 0.2f;
    }

    @Override
    public float getHeight() {
        return 0.2f;
    }

    @Override
    public float getGravity() {
        return 0.07f;
    }

    @Override
    public float getDrag() {
        return 0.05f;
    }

    @Override
    public boolean onUpdate(int n) {
        boolean bl;
        boolean bl2 = super.onUpdate(n);
        if (this.l != 0L) {
            Entity entity = this.level.getEntity(this.l);
            if (entity == null || !entity.isAlive()) {
                this.caughtEntity = null;
                this.setTarget(0L);
            } else {
                this.setPosition(new Vector3(entity.x, entity.y + (double)(this.getHeight() * 0.75f), entity.z));
            }
            bl2 = true;
        }
        if (bl2) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        if (bl = this.isInsideOfWater()) {
            this.motionX = 0.0;
            this.motionY -= (double)this.getGravity() * -0.04;
            this.motionZ = 0.0;
            bl2 = true;
        } else if (this.isCollided && this.keepMovement) {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
            this.keepMovement = false;
            bl2 = true;
        }
        if (bl) {
            if (this.waitTimer == 240) {
                this.waitTimer = this.waitChance << 1;
            } else if (this.waitTimer == 360) {
                this.waitTimer = this.waitChance * 3;
            }
            if (!this.attracted) {
                if (this.waitTimer > 0) {
                    --this.waitTimer;
                }
                if (this.waitTimer == 0) {
                    if (Utils.random.nextInt(100) < 90) {
                        this.attractTimer = Utils.random.nextInt(40) + 20;
                        this.spawnFish();
                        this.caught = false;
                        this.attracted = true;
                    } else {
                        this.waitTimer = this.waitChance;
                    }
                }
            } else if (!this.caught) {
                if (this.attractFish()) {
                    this.caughtTimer = Utils.random.nextInt(20) + 30;
                    this.fishBites();
                    this.caught = true;
                }
            } else {
                if (this.caughtTimer > 0) {
                    --this.caughtTimer;
                }
                if (this.caughtTimer == 0) {
                    this.attracted = false;
                    this.caught = false;
                    this.waitTimer = this.waitChance * 3;
                }
            }
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl2;
    }

    public int getWaterHeight() {
        for (int k = this.getFloorY(); k < 256; ++k) {
            int n = this.level.getBlockIdAt(this.chunk, this.getFloorX(), k, this.getFloorZ());
            if (n != 0) continue;
            return k;
        }
        return this.getFloorY();
    }

    public void fishBites() {
        Collection<Player> collection = this.getViewers().values();
        EntityEventPacket entityEventPacket = new EntityEventPacket();
        entityEventPacket.eid = this.getId();
        entityEventPacket.event = 13;
        Server.broadcastPacket(collection, (DataPacket)entityEventPacket);
        EntityEventPacket entityEventPacket2 = new EntityEventPacket();
        entityEventPacket2.eid = this.getId();
        entityEventPacket2.event = 11;
        Server.broadcastPacket(collection, (DataPacket)entityEventPacket2);
        EntityEventPacket entityEventPacket3 = new EntityEventPacket();
        entityEventPacket3.eid = this.getId();
        entityEventPacket3.event = 14;
        Server.broadcastPacket(collection, (DataPacket)entityEventPacket3);
        this.level.addParticle(new BubbleParticle(this.setComponents(this.x + Utils.random.nextDouble() * 0.5 - 0.25, this.getWaterHeight(), this.z + Utils.random.nextDouble() * 0.5 - 0.25)), null, 5);
    }

    public void spawnFish() {
        this.fish = new Vector3(this.x + (Utils.random.nextDouble() * 1.2 + 1.0) * (double)(Utils.random.nextBoolean() ? -1 : 1), this.getWaterHeight(), this.z + (Utils.random.nextDouble() * 1.2 + 1.0) * (double)(Utils.random.nextBoolean() ? -1 : 1));
    }

    public boolean attractFish() {
        double d2;
        double d3 = 0.1;
        this.fish.setComponents(this.fish.x + (this.x - this.fish.x) * d3, this.fish.y, this.fish.z + (this.z - this.fish.z) * d3);
        if (Utils.random.nextInt(100) < 85) {
            this.level.addParticle(new WaterParticle(this.fish));
        }
        return (d2 = Math.abs(Math.sqrt(this.x * this.x + this.z * this.z) - Math.sqrt(this.fish.x * this.fish.x + this.fish.z * this.fish.z))) < 0.15;
    }

    public void reelLine() {
        if (this.shootingEntity instanceof Player) {
            Location location;
            if (this.caught) {
                Enchantment enchantment;
                location = (Player)this.shootingEntity;
                Item item = Fishing.getFishingResult(this.rod);
                if (item instanceof ItemBookEnchanted) {
                    if (!item.hasEnchantments()) {
                        item = item.clone();
                        enchantment = Enchantment.getEnchantment(Utils.rand(0, 36));
                        if (Utils.random.nextDouble() < 0.3) {
                            enchantment.setLevel(Utils.rand(1, enchantment.getMaxLevel()));
                        }
                        item.addEnchantment(enchantment);
                    }
                } else if (item instanceof ItemFishingRod && Utils.rand(1, 3) == 2 && !item.hasEnchantments()) {
                    item = item.clone();
                    enchantment = Enchantment.getEnchantment(k[Utils.rand(0, 4)]);
                    if (Utils.random.nextDouble() < 0.3) {
                        enchantment.setLevel(Utils.rand(1, enchantment.getMaxLevel()));
                    }
                    item.addEnchantment(enchantment);
                }
                int n = Utils.random.nextInt(3) + 1;
                Location location2 = location.subtract(this).multiply(0.1);
                location2.y += Math.sqrt(location.distance(this)) * 0.08;
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player)location, this, item, n, location2);
                this.getServer().getPluginManager().callEvent(playerFishEvent);
                if (!playerFishEvent.isCancelled()) {
                    EntityItem entityItem = new EntityItem(this.level.getChunk(this.getChunkX(), this.getChunkZ(), true), Entity.getDefaultNBT(new Vector3(this.x, this.getWaterHeight(), this.z), playerFishEvent.getMotion(), ThreadLocalRandom.current().nextFloat() * 360.0f, 0.0f).putShort("Health", 5).putCompound("Item", NBTIO.putItemHelper(playerFishEvent.getLoot())).putShort("PickupDelay", 1));
                    entityItem.setOwner(((Player)location).getName());
                    entityItem.spawnToAll();
                    location.getLevel().dropExpOrb(location, playerFishEvent.getExperience());
                }
            }
            if (this.caughtEntity != null) {
                location = this.shootingEntity.subtract(this).multiply(0.1);
                location.y += Math.sqrt(this.shootingEntity.distance(this)) * 0.08;
                this.caughtEntity.setMotion(location);
            }
        }
        this.close();
    }

    @Override
    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.entityRuntimeId = this.getId();
        addEntityPacket.entityUniqueId = this.getId();
        addEntityPacket.type = 77;
        addEntityPacket.x = (float)this.x;
        addEntityPacket.y = (float)this.y;
        addEntityPacket.z = (float)this.z;
        addEntityPacket.speedX = (float)this.motionX;
        addEntityPacket.speedY = (float)this.motionY;
        addEntityPacket.speedZ = (float)this.motionZ;
        addEntityPacket.yaw = (float)this.yaw;
        addEntityPacket.pitch = (float)this.pitch;
        long l = -1L;
        if (this.shootingEntity != null) {
            l = this.shootingEntity.getId();
        }
        addEntityPacket.metadata = this.dataProperties.putLong(5, l).clone();
        return addEntityPacket;
    }

    @Override
    public boolean canCollide() {
        return this.canCollide;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.server.getPluginManager().callEvent(new ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity)));
        float f2 = this.getResultDamage();
        EntityDamageByEntityEvent entityDamageByEntityEvent = this.shootingEntity == null ? new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.PROJECTILE, f2, this.knockBack) : new EntityDamageByChildEntityEvent(this.shootingEntity, (Entity)this, entity, EntityDamageEvent.DamageCause.PROJECTILE, f2, this.knockBack);
        if (entity.attack(entityDamageByEntityEvent)) {
            this.caughtEntity = entity;
            this.setTarget(entity.getId());
        }
    }

    public void checkLure() {
        Enchantment enchantment;
        if (this.rod != null && (enchantment = this.rod.getEnchantment(24)) != null) {
            this.waitChance = 120 - 25 * enchantment.getLevel();
        }
    }

    public void setTarget(long l) {
        this.l = l;
        this.setDataProperty(new LongEntityData(6, l));
        this.canCollide = l == 0L;
    }
}


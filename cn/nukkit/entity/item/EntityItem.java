/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ItemDespawnEvent;
import cn.nukkit.event.entity.ItemSpawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsHistory;

public class EntityItem
extends Entity {
    public static final int NETWORK_ID = 64;
    protected String owner;
    protected String thrower;
    protected Item item;
    protected int pickupDelay;
    public Player droppedBy;
    private boolean k;

    public EntityItem(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 64;
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
        return 0.25f;
    }

    @Override
    public float getGravity() {
        return 0.04f;
    }

    @Override
    public float getDrag() {
        return 0.02f;
    }

    @Override
    protected float getBaseOffset() {
        return 0.125f;
    }

    @Override
    public boolean canCollide() {
        return false;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(5);
        super.initEntity();
        if (this.namedTag.contains("Health")) {
            this.setHealth(this.namedTag.getShort("Health"));
        } else {
            this.setHealth(5.0f);
        }
        if (this.namedTag.contains("Age")) {
            this.age = this.namedTag.getShort("Age");
        }
        if (this.namedTag.contains("PickupDelay")) {
            this.pickupDelay = this.namedTag.getShort("PickupDelay");
        }
        if (this.namedTag.contains("Owner")) {
            this.owner = this.namedTag.getString("Owner");
        }
        if (this.namedTag.contains("Thrower")) {
            this.thrower = this.namedTag.getString("Thrower");
        }
        if (!this.namedTag.contains("Item")) {
            this.close();
            return;
        }
        this.item = NBTIO.getItemHelper(this.namedTag.getCompound("Item"));
        int n = this.item.getId();
        if (n >= 742 && n <= 752) {
            this.fireProof = true;
        }
        this.server.getPluginManager().callEvent(new ItemSpawnEvent(this));
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();
        if (!(damageCause != EntityDamageEvent.DamageCause.VOID && damageCause != EntityDamageEvent.DamageCause.CONTACT && damageCause != EntityDamageEvent.DamageCause.FIRE_TICK && (damageCause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && damageCause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || this.isInsideOfWater() || this.item != null && this.item.getId() == 399) || !super.attack(entityDamageEvent))) {
            if (this.item == null || this.isAlive() || this.k) {
                return true;
            }
            this.k = true;
            int n = this.item.getId();
            if (n != 218 && n != 205) {
                return true;
            }
            CompoundTag compoundTag = this.item.getNamedTag();
            if (compoundTag == null) {
                return true;
            }
            ListTag<CompoundTag> listTag = compoundTag.getList("Items", CompoundTag.class);
            for (int k = 0; k < listTag.size(); ++k) {
                CompoundTag compoundTag2 = listTag.get(k);
                Item item = NBTIO.getItemHelper(compoundTag2);
                if (item.isNull()) continue;
                this.level.dropItem(this, item);
            }
            return true;
        }
        return false;
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
        if (this.timing != null) {
            this.timing.startTiming();
        }
        if (!this.fireProof && this.isInsideOfFire()) {
            this.close();
            if (this.timing != null) {
                this.timing.stopTiming();
            }
            return true;
        }
        boolean bl = this.entityBaseTick(n2);
        if (this.isAlive()) {
            Entity[] entityArray;
            if (this.pickupDelay > 0 && this.pickupDelay < Short.MAX_VALUE) {
                this.pickupDelay -= n2;
                if (this.pickupDelay < 0) {
                    this.pickupDelay = 0;
                }
            }
            if (this.age > 6000) {
                entityArray = new ItemDespawnEvent(this);
                this.server.getPluginManager().callEvent((Event)entityArray);
                if (entityArray.isCancelled()) {
                    this.age = 0;
                } else {
                    this.close();
                    if (this.timing != null) {
                        this.timing.stopTiming();
                    }
                    return true;
                }
            }
            if (this.age % 200 == 0 && this.onGround && this.item != null && this.item.getCount() < this.item.getMaxStackSize()) {
                for (Entity entity : entityArray = this.getLevel().getNearbyEntities(this.getBoundingBox().grow(1.0, 1.0, 1.0), this)) {
                    if (entity instanceof EntityItem) {
                        int n3;
                        Item item;
                        if (entity.closed || !entity.isAlive() || !(item = ((EntityItem)entity).item).equals(this.item, true, true) || !entity.isOnGround() || (n3 = this.item.getCount() + item.getCount()) > this.item.getMaxStackSize()) continue;
                        item.setCount(0);
                        entity.close();
                        this.item.setCount(n3);
                        EntityEventPacket entityEventPacket = new EntityEventPacket();
                        entityEventPacket.eid = this.getId();
                        entityEventPacket.data = n3;
                        entityEventPacket.event = 69;
                        Server.broadcastPacket(this.getViewers().values(), (DataPacket)entityEventPacket);
                    }
                    if (this.item.getCount() >= this.item.getMaxStackSize()) break;
                }
            }
            if (this.isInsideOfWater()) {
                this.motionY = (double)this.getGravity() - 0.06;
            } else if (!this.isOnGround()) {
                this.motionY -= (double)this.getGravity();
            }
            if (this.checkObstruction(this.x, this.y, this.z)) {
                bl = true;
            }
            double d2 = 1.0f - this.getDrag();
            if (this.onGround && (Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5)) {
                d2 *= this.getLevel().getBlock(this.chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ(), false).getFrictionFactor();
            }
            this.motionX *= d2;
            this.motionY *= (double)(1.0f - this.getDrag());
            this.motionZ *= d2;
            if (this.onGround) {
                this.motionY *= -0.5;
            }
            if (this.move(this.motionX, this.motionY, this.motionZ)) {
                this.updateMovement();
            }
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl || !this.onGround || Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionY) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        if (this.item != null) {
            this.namedTag.putCompound("Item", NBTIO.putItemHelper(this.item, -1));
            this.namedTag.putShort("Health", (int)this.getHealth());
            this.namedTag.putShort("Age", this.age);
            this.namedTag.putShort("PickupDelay", this.pickupDelay);
            if (this.owner != null) {
                this.namedTag.putString("Owner", this.owner);
            }
            if (this.thrower != null) {
                this.namedTag.putString("Thrower", this.thrower);
            }
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : (this.item.hasCustomName() ? this.item.getCustomName() : this.item.getName());
    }

    public Item getItem() {
        return this.item;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    public int getPickupDelay() {
        return this.pickupDelay;
    }

    public void setPickupDelay(int n) {
        this.pickupDelay = n;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String string) {
        this.owner = string;
    }

    public String getThrower() {
        return this.thrower;
    }

    public void setThrower(String string) {
        this.thrower = string;
    }

    @Override
    public DataPacket createAddEntityPacket() {
        AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
        addItemEntityPacket.entityUniqueId = this.getId();
        addItemEntityPacket.entityRuntimeId = this.getId();
        addItemEntityPacket.x = (float)this.x;
        addItemEntityPacket.y = (float)this.y + this.getBaseOffset();
        addItemEntityPacket.z = (float)this.z;
        addItemEntityPacket.speedX = (float)this.motionX;
        addItemEntityPacket.speedY = (float)this.motionY;
        addItemEntityPacket.speedZ = (float)this.motionZ;
        addItemEntityPacket.metadata = this.dataProperties.clone();
        if (this.server.reduceTraffic) {
            Item item = Item.get(this.item.getId(), this.item.getDamage(), this.item.getCount());
            if (this.item.hasEnchantments()) {
                item.addEnchantment(Enchantment.get(0));
            }
            addItemEntityPacket.item = item;
        } else {
            addItemEntityPacket.item = this.item;
        }
        return addItemEntityPacket;
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (Timings.entityBaseTickTimer != null) {
            Timings.entityBaseTickTimer.startTiming();
        }
        this.collisionBlocks = null;
        this.justCreated = false;
        if (!this.isAlive()) {
            this.despawnFromAll();
            this.close();
            if (Timings.entityBaseTickTimer != null) {
                Timings.entityBaseTickTimer.stopTiming();
            }
            return false;
        }
        boolean bl = false;
        this.checkBlockCollision();
        if (this.y <= -16.0 && this.isAlive()) {
            this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.VOID, 10.0f));
            bl = true;
        }
        if (this.fireTicks > 0) {
            if (this.fireProof) {
                this.fireTicks -= n << 2;
                if (this.fireTicks < 0) {
                    this.fireTicks = 0;
                }
            } else {
                if (this.fireTicks % 20 == 0 || n > 20) {
                    this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.FIRE_TICK, 1.0f));
                }
                this.fireTicks -= n;
            }
            if (this.fireTicks <= 0) {
                this.extinguish();
            } else if (!this.fireProof) {
                this.setDataFlag(0, 0, true);
                bl = true;
            }
        }
        if (this.noDamageTicks > 0) {
            this.noDamageTicks -= n;
            if (this.noDamageTicks < 0) {
                this.noDamageTicks = 0;
            }
        }
        this.age += n;
        ++TimingsHistory.activatedEntityTicks;
        if (Timings.entityBaseTickTimer != null) {
            Timings.entityBaseTickTimer.stopTiming();
        }
        return bl;
    }
}


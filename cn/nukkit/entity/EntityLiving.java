/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityDamageable;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.EntitySwimming;
import cn.nukkit.entity.EntityTameable;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.entity.mob.EntityDrowned;
import cn.nukkit.entity.mob.EntitySkeleton;
import cn.nukkit.entity.mob.EntityStray;
import cn.nukkit.entity.passive.EntityWolf;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.weather.EntityWeather;
import cn.nukkit.event.entity.EntityDamageBlockedEvent;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.item.ItemTurtleShell;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.BubbleParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.utils.BlockIterator;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public abstract class EntityLiving
extends Entity
implements EntityDamageable {
    protected int attackTime;
    private float l;
    protected float movementSpeed = 0.1f;
    protected int turtleTicks;
    private boolean m;
    private boolean k;
    protected final boolean isDrowned = this instanceof EntityDrowned;

    public EntityLiving(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected float getGravity() {
        return 0.08f;
    }

    @Override
    protected float getDrag() {
        return 0.02f;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        if (this.namedTag.contains("HealF")) {
            this.namedTag.putFloat("Health", this.namedTag.getShort("HealF"));
            this.namedTag.remove("HealF");
        }
        if (!this.namedTag.contains("Health") || !(this.namedTag.get("Health") instanceof FloatTag)) {
            this.namedTag.putFloat("Health", this.getMaxHealth());
        }
        this.health = this.namedTag.getFloat("Health");
    }

    @Override
    public void setHealth(float f2) {
        boolean bl = this.isAlive();
        super.setHealth(f2);
        if (this.isAlive() && !bl) {
            EntityEventPacket entityEventPacket = new EntityEventPacket();
            entityEventPacket.eid = this.getId();
            entityEventPacket.event = 18;
            Server.broadcastPacket(this.hasSpawned.values(), (DataPacket)entityEventPacket);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putFloat("Health", this.getHealth());
    }

    public boolean hasLineOfSight(Entity entity) {
        return true;
    }

    public void collidingWith(Entity entity) {
        entity.applyEntityCollision(this);
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (this.noDamageTicks > 0) {
            return false;
        }
        float f2 = entityDamageEvent.getDamage();
        if (this.attackTime > 0) {
            if (f2 > this.l) {
                entityDamageEvent.setDamage(Math.max(0.0f, f2 - this.l));
            } else {
                return false;
            }
        }
        if (this.blockedByShield(entityDamageEvent)) {
            if (f2 > this.l) {
                this.l = f2;
            }
            return false;
        }
        boolean bl = super.attack(entityDamageEvent);
        if (bl) {
            Cloneable cloneable;
            if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
                cloneable = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager();
                if (entityDamageEvent instanceof EntityDamageByChildEntityEvent) {
                    cloneable = ((EntityDamageByChildEntityEvent)entityDamageEvent).getChild();
                }
                if (((Entity)cloneable).isOnFire() && !(cloneable instanceof Player)) {
                    this.setOnFire(this.server.getDifficulty() << 1);
                }
                double d2 = this.x - ((Entity)cloneable).x;
                double d3 = this.z - ((Entity)cloneable).z;
                this.knockBack((Entity)cloneable, entityDamageEvent.getDamage(), d2, d3, ((EntityDamageByEntityEvent)entityDamageEvent).getKnockBack());
            }
            cloneable = new EntityEventPacket();
            ((EntityEventPacket)cloneable).eid = this.getId();
            ((EntityEventPacket)cloneable).event = this.getHealth() < 1.0f ? 3 : 2;
            Server.broadcastPacket(this.hasSpawned.values(), (DataPacket)cloneable);
        }
        if (!entityDamageEvent.isCancelled()) {
            this.a(entityDamageEvent);
            if (f2 > this.l) {
                this.l = f2;
            }
            this.scheduleUpdate();
        }
        return bl;
    }

    protected boolean blockedByShield(EntityDamageEvent entityDamageEvent) {
        Entity entity;
        if (!this.isBlocking()) {
            return false;
        }
        Entity entity2 = entityDamageEvent instanceof EntityDamageByChildEntityEvent ? ((EntityDamageByChildEntityEvent)entityDamageEvent).getChild() : (entity = entityDamageEvent instanceof EntityDamageByEntityEvent ? ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() : null);
        if (entity == null || entity instanceof EntityWeather) {
            return false;
        }
        Position position = entity.getPosition();
        Vector3 vector3 = this.getDirectionVector();
        Vector3 vector32 = this.getPosition().subtract(position).normalize();
        boolean bl = vector32.x * vector3.x + vector32.z * vector3.z < 0.0;
        boolean bl2 = !(entity instanceof EntityProjectile);
        EntityDamageBlockedEvent entityDamageBlockedEvent = new EntityDamageBlockedEvent(this, entityDamageEvent, bl2, true);
        if (!bl || !entityDamageEvent.canBeReducedByArmor() || entity instanceof EntityProjectile && ((EntityProjectile)entity).piercing > 0) {
            entityDamageBlockedEvent.setCancelled();
        }
        this.getServer().getPluginManager().callEvent(entityDamageBlockedEvent);
        if (entityDamageBlockedEvent.isCancelled()) {
            return false;
        }
        if (entityDamageBlockedEvent.getKnockBackAttacker() && entity instanceof EntityLiving) {
            double d2 = entity.getX() - this.getX();
            double d3 = entity.getZ() - this.getZ();
            this.a(entityDamageEvent);
            ((EntityLiving)entity).knockBack(this, 0.0, d2, d3, 0.25);
        }
        this.onBlock(entity, entityDamageBlockedEvent.getAnimation(), entityDamageEvent.getDamage());
        return true;
    }

    private void a(EntityDamageEvent entityDamageEvent) {
        if (this.attackTime < 1) {
            this.attackTime = entityDamageEvent.getAttackCooldown();
        }
    }

    protected void onBlock(Entity entity, boolean bl, float f2) {
        if (bl) {
            this.level.addLevelSoundEvent(this, 255);
        }
    }

    public void knockBack(Entity entity, double d2, double d3, double d4) {
        this.knockBack(entity, d2, d3, d4, 0.3);
    }

    public void knockBack(Entity entity, double d2, double d3, double d4, double d5) {
        double d6 = Math.sqrt(d3 * d3 + d4 * d4);
        if (d6 <= 0.0) {
            return;
        }
        if (this instanceof Player) {
            int n = 0;
            for (Item item : ((Player)this).getInventory().getArmorContents()) {
                if (item.getTier() != 6) continue;
                ++n;
            }
            if (n > 0) {
                d5 *= 1.0 - 0.225 * (double)n;
            }
        }
        d6 = 1.0 / d6;
        Vector3 vector3 = new Vector3(this.motionX, this.motionY, this.motionZ);
        vector3.x /= 2.0;
        vector3.y /= 2.0;
        vector3.z /= 2.0;
        vector3.x += d3 * d6 * d5;
        vector3.y += d5;
        vector3.z += d4 * d6 * d5;
        if (vector3.y > d5) {
            vector3.y = d5;
        }
        this.resetFallDistance();
        this.setMotion(vector3);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void kill() {
        Item[] itemArray;
        if (!this.isAlive()) {
            return;
        }
        super.kill();
        EntityDeathEvent entityDeathEvent = new EntityDeathEvent(this, this.getDrops());
        this.server.getPluginManager().callEvent(entityDeathEvent);
        this.a();
        int n = entityDeathEvent.getEntity().getNetworkId();
        if ((n == 38 || n == 36 || n == 35 || n == 40) && entityDeathEvent.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent && (itemArray = ((EntityDamageByEntityEvent)entityDeathEvent.getEntity().getLastDamageCause()).getDamager()) instanceof Player) {
            ((Player)itemArray).awardAchievement("killEnemy");
        }
        if (this.level.getGameRules().getBoolean(GameRule.DO_MOB_LOOT) && this.lastDamageCause != null && EntityDamageEvent.DamageCause.VOID != this.lastDamageCause.getCause()) {
            if (entityDeathEvent.getEntity() instanceof BaseEntity && (itemArray = (BaseEntity)entityDeathEvent.getEntity()).getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                Entity entity = ((EntityDamageByEntityEvent)itemArray.getLastDamageCause()).getDamager();
                if (entity instanceof Player) {
                    this.getLevel().dropExpOrb(this, itemArray.getKillExperience());
                    if (!this.dropsOnNaturalDeath()) {
                        for (Item item : entityDeathEvent.getDrops()) {
                            this.getLevel().dropItem(this, item);
                        }
                    }
                } else if (entity instanceof EntityCreeper && entity != this && this.lastDamageCause.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                    Item item;
                    if (((EntityCreeper)entity).isPowered() && (item = ItemSkull.getMobHead(this.getNetworkId())) != null) {
                        this.getLevel().dropItem(this, item);
                    }
                } else if (itemArray instanceof EntityCreeper && (entity instanceof EntitySkeleton || entity instanceof EntityStray) && this.lastDamageCause.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                    this.getLevel().dropItem(this, Item.get(Utils.rand(500, 511), 0, 1));
                }
            }
            if (this.dropsOnNaturalDeath()) {
                void var5_10;
                itemArray = entityDeathEvent.getDrops();
                int n2 = itemArray.length;
                boolean bl = false;
                while (var5_10 < n2) {
                    Item item = itemArray[var5_10];
                    this.getLevel().dropItem(this, item);
                    ++var5_10;
                }
            }
        }
    }

    @Override
    public boolean entityBaseTick(int n) {
        int n2;
        if (Timings.livingEntityBaseTickTimer != null) {
            Timings.livingEntityBaseTickTimer.startTiming();
        }
        boolean bl = this.isSubmerged();
        if (this instanceof Player && !this.closed) {
            Player player = (Player)this;
            n2 = !bl ? 1 : 0;
            Entity[] entityArray = player.getInventory();
            if (n2 != 0 && entityArray != null && entityArray.getHelmetFast() instanceof ItemTurtleShell) {
                this.turtleTicks = 200;
            } else if (this.turtleTicks > 0) {
                n2 = 1;
                --this.turtleTicks;
            }
            if (player.isCreative() || player.isSpectator()) {
                n2 = 1;
            }
            this.setDataFlagSelfOnly(0, 35, n2 != 0);
        }
        boolean bl2 = super.entityBaseTick(n);
        if (this.isAlive()) {
            if (this.isInsideOfSolid()) {
                bl2 = true;
                this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.SUFFOCATION, 1.0f));
            }
            if (this.isOnLadder() || this.hasEffect(24) || this.hasEffect(27)) {
                this.resetFallDistance();
            }
            if (bl && !this.hasEffect(13)) {
                if (this instanceof EntitySwimming || this.isDrowned || this instanceof Player && (((Player)this).isCreative() || ((Player)this).isSpectator())) {
                    this.setAirTicks(400);
                } else if (this.turtleTicks == 0) {
                    bl2 = true;
                    n2 = this.getAirTicks() - n;
                    if (n2 <= -20) {
                        n2 = 0;
                        if (!(this instanceof Player) || this.level.getGameRules().getBoolean(GameRule.DROWNING_DAMAGE)) {
                            this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.DROWNING, 2.0f));
                        }
                    }
                    this.setAirTicks(n2);
                }
            } else if (this instanceof EntitySwimming) {
                bl2 = true;
                n2 = this.getAirTicks() - n;
                if (n2 <= -20) {
                    n2 = 0;
                    this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.SUFFOCATION, 2.0f));
                }
                this.setAirTicks(n2);
            } else {
                n2 = this.getAirTicks();
                if (n2 < 400) {
                    this.setAirTicks(Math.min(400, n2 + n * 5));
                }
            }
            if (this instanceof Player && this.age % 5 == 0) {
                n2 = this.level.getBlockIdAt(this.chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ());
                if (n2 == 81) {
                    Block.get(81).onEntityCollide(this);
                } else if (n2 == 213) {
                    Block.get(213).onEntityCollide(this);
                    if (this.isInsideOfWater()) {
                        this.level.addParticle(new BubbleParticle(this));
                        this.setMotion(this.getMotion().add(0.0, -0.3, 0.0));
                    }
                }
            }
            if (this.attackTime > 0) {
                this.attackTime -= n;
                if (this.attackTime < 1) {
                    this.l = 0.0f;
                }
                bl2 = true;
            }
            if (this.riding == null) {
                Entity[] entityArray;
                for (Entity entity : entityArray = this.level.getNearbyEntities(this.boundingBox.grow(0.2f, 0.0, 0.2f), this)) {
                    if (!(entity instanceof EntityRideable)) continue;
                    this.collidingWith(entity);
                }
            }
        }
        if (Timings.livingEntityBaseTickTimer != null) {
            Timings.livingEntityBaseTickTimer.stopTiming();
        }
        return bl2;
    }

    public Item[] getDrops() {
        return new Item[0];
    }

    public Block[] getLineOfSight(int n) {
        return this.getLineOfSight(n, 0);
    }

    public Block[] getLineOfSight(int n, int n2) {
        return this.getLineOfSight(n, n2, new Integer[0]);
    }

    public Block[] getLineOfSight(int n, int n2, Map<Integer, Object> map) {
        return this.getLineOfSight(n, n2, map.keySet().toArray(new Integer[0]));
    }

    public Block[] getLineOfSight(int n, int n2, Integer[] integerArray) {
        if (n > 120) {
            n = 120;
        }
        if (integerArray != null && integerArray.length == 0) {
            integerArray = null;
        }
        ArrayList<Block> arrayList = new ArrayList<Block>();
        BlockIterator blockIterator = new BlockIterator(this.level, this.getPosition(), this.getDirectionVector(), this.getEyeHeight(), n);
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            arrayList.add(block);
            if (n2 != 0 && arrayList.size() > n2) {
                arrayList.remove(0);
            }
            int n3 = block.getId();
            if (!(integerArray == null ? n3 != 0 : Arrays.binarySearch((Object[])integerArray, (Object)n3) < 0)) continue;
            break;
        }
        return arrayList.toArray(new Block[0]);
    }

    public Block getTargetBlock(int n) {
        return this.getTargetBlock(n, new Integer[0]);
    }

    public Block getTargetBlock(int n, Map<Integer, Object> map) {
        return this.getTargetBlock(n, map.keySet().toArray(new Integer[0]));
    }

    public Block getTargetBlock(int n, Integer[] integerArray) {
        block4: {
            try {
                Block[] blockArray = this.getLineOfSight(n, 1, integerArray);
                Block block = blockArray[0];
                if (block == null) break block4;
                if (integerArray != null && integerArray.length != 0) {
                    if (Arrays.binarySearch((Object[])integerArray, (Object)block.getId()) < 0) {
                        return block;
                    }
                    break block4;
                }
                return block;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return null;
    }

    public void setMovementSpeed(float f2) {
        this.movementSpeed = f2;
    }

    public float getMovementSpeed() {
        return this.movementSpeed;
    }

    public int getAirTicks() {
        return this.airTicks;
    }

    public void setAirTicks(int n) {
        this.airTicks = n;
    }

    public boolean isBlocking() {
        return this.m;
    }

    public void setBlocking(boolean bl) {
        if (this.m != bl) {
            this.m = bl;
            this.setDataFlag(92, 72, bl);
        }
    }

    public boolean dropsOnNaturalDeath() {
        return true;
    }

    public boolean isSpinAttack() {
        return this.k;
    }

    public void setSpinAttack(boolean bl) {
        if (this.k != bl) {
            this.k = bl;
            this.setDataFlag(0, 56, bl);
        }
    }

    private void a() {
        if (this instanceof EntityTameable) {
            Cloneable cloneable;
            if (!((EntityTameable)((Object)this)).hasOwner()) {
                return;
            }
            if (((EntityTameable)((Object)this)).getOwner() == null) {
                return;
            }
            String string = this instanceof EntityWolf ? "%entity.wolf.name" : this.getName();
            TranslationContainer translationContainer = new TranslationContainer("death.attack.generic", string);
            if (this.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                cloneable = ((EntityDamageByEntityEvent)this.getLastDamageCause()).getDamager();
                if (cloneable instanceof Player) {
                    translationContainer = new TranslationContainer("death.attack.player", string, ((Entity)cloneable).getName());
                } else {
                    if (cloneable instanceof EntityWolf) {
                        ((EntityWolf)cloneable).setAngry(false);
                    }
                    translationContainer = new TranslationContainer("death.attack.mob", string, ((Entity)cloneable).getName());
                }
            }
            cloneable = new TextPacket();
            ((TextPacket)cloneable).type = (byte)2;
            ((TextPacket)cloneable).message = translationContainer.getText();
            ((TextPacket)cloneable).parameters = translationContainer.getParameters();
            ((TextPacket)cloneable).isLocalized = true;
            ((EntityTameable)((Object)this)).getOwner().dataPacket((DataPacket)cloneable);
        }
    }

    private static Exception b(Exception exception) {
        return exception;
    }
}


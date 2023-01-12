/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.EntityBoss;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.a;
import cn.nukkit.entity.mob.EntityEnderDragon;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.entity.mob.EntityRavager;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.entity.passive.EntityCow;
import cn.nukkit.entity.passive.EntityWolf;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timings;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseEntity
extends EntityCreature
implements EntityAgeable {
    public int stayTime = 0;
    protected int moveTime = 0;
    protected float moveMultiplier = 1.0f;
    protected Vector3 target;
    protected Entity followTarget;
    protected Player lastInteract;
    protected int attackDelay = 0;
    private short r = 0;
    private short q = 0;
    private boolean o = false;
    private boolean p = true;
    private boolean s = false;
    public Item[] armor;
    private static final Int2ObjectMap<Float> n = new a();

    public BaseEntity(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    public abstract Vector3 updateMove(int var1);

    public abstract int getKillExperience();

    public boolean isFriendly() {
        return this.s;
    }

    public boolean isMovement() {
        return this.getServer().getMobAiEnabled() && this.p;
    }

    public boolean isKnockback() {
        return this.attackTime > 0;
    }

    public void setFriendly(boolean bl) {
        this.s = bl;
    }

    public void setMovement(boolean bl) {
        this.p = bl;
    }

    public double getSpeed() {
        if (this.o) {
            return 1.2;
        }
        return 1.0;
    }

    public int getAge() {
        return this.age;
    }

    public Entity getTarget() {
        return this.followTarget != null ? this.followTarget : (this.target instanceof Entity ? (Entity)this.target : null);
    }

    public void setTarget(Entity entity) {
        this.followTarget = entity;
        this.moveTime = 0;
        this.stayTime = 0;
        this.target = null;
    }

    @Override
    public boolean isBaby() {
        return this.o;
    }

    @Override
    public void setBaby(boolean bl) {
        this.o = bl;
        this.setDataFlag(0, 11, bl);
        if (bl) {
            this.setScale(0.5f);
            this.age = Utils.rand(-2400, -1800);
        } else {
            this.setScale(1.0f);
        }
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        if (this.namedTag.contains("Movement")) {
            this.setMovement(this.namedTag.getBoolean("Movement"));
        }
        if (this.namedTag.contains("Age")) {
            this.age = this.namedTag.getShort("Age");
        }
        if (this.namedTag.getBoolean("Baby")) {
            this.setBaby(true);
        }
        if (this.namedTag.contains("InLoveTicks")) {
            this.r = (short)this.namedTag.getShort("InLoveTicks");
        }
        if (this.namedTag.contains("InLoveCooldown")) {
            this.q = (short)this.namedTag.getShort("InLoveCooldown");
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Baby", this.o);
        this.namedTag.putBoolean("Movement", this.isMovement());
        this.namedTag.putShort("Age", this.age);
        if (this.isInLove()) {
            this.namedTag.putShort("InLoveTicks", this.r);
        }
        if (this.isInLoveCooldown()) {
            this.namedTag.putShort("InLoveCooldown", this.q);
        }
    }

    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (this instanceof EntityMob) {
            if (entityCreature instanceof Player) {
                Player player = (Player)entityCreature;
                return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && d2 <= 100.0;
            }
            return entityCreature.isAlive() && !entityCreature.closed && d2 <= 100.0;
        }
        if (this instanceof EntityAnimal && this.isInLove()) {
            return entityCreature instanceof BaseEntity && ((BaseEntity)entityCreature).isInLove() && entityCreature.isAlive() && !entityCreature.closed && entityCreature.getNetworkId() == this.getNetworkId() && d2 <= 100.0;
        }
        return false;
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (Timings.entityBaseTickTimer != null) {
            Timings.entityBaseTickTimer.startTiming();
        }
        if (this.canDespawn() && this.age > Server.getInstance().mobDespawnTicks && !this.hasCustomName() && !(this instanceof EntityBoss)) {
            this.close();
            if (Timings.entityBaseTickTimer != null) {
                Timings.entityBaseTickTimer.stopTiming();
            }
            return true;
        }
        if (this instanceof EntityMob && this.attackDelay < 200) {
            ++this.attackDelay;
        }
        boolean bl = super.entityBaseTick(n);
        if (this.moveTime > 0) {
            this.moveTime -= n;
        }
        if (this.isBaby() && this.age > 0) {
            this.setBaby(false);
        }
        if (this.isInLove()) {
            this.r = (short)(this.r - n);
            if (!this.isBaby() && this.age > 0 && this.age % 20 == 0) {
                Entity entity;
                Entity[] entityArray;
                for (int k = 0; k < 3; ++k) {
                    this.level.addParticle(new HeartParticle(this.add(Utils.rand(-1.0, 1.0), (double)this.getMountedYOffset() + Utils.rand(-1.0, 1.0), Utils.rand(-1.0, 1.0))));
                }
                Entity[] entityArray2 = entityArray = this.level.getCollidingEntities(this.boundingBox.grow(0.5, 0.5, 0.5));
                int n2 = entityArray2.length;
                for (int k = 0; !(k >= n2 || (entity = entityArray2[k]) != this && this.a(entity)); ++k) {
                }
            }
        } else if (this.isInLoveCooldown()) {
            this.q = (short)(this.q - n);
        }
        if (Timings.entityBaseTickTimer != null) {
            Timings.entityBaseTickTimer.stopTiming();
        }
        return bl;
    }

    private boolean a(Entity entity) {
        BaseEntity baseEntity;
        if (entity instanceof BaseEntity && entity.getNetworkId() == this.getNetworkId() && (baseEntity = (BaseEntity)entity).isInLove() && !baseEntity.isBaby() && baseEntity.age > 0) {
            Player player = baseEntity.lastInteract;
            baseEntity.lastInteract = null;
            this.setInLove(false);
            baseEntity.setInLove(false);
            this.q = (short)1200;
            baseEntity.q = (short)1200;
            this.stayTime = 60;
            baseEntity.stayTime = 60;
            int n = 0;
            for (Entity entity2 : this.chunk.getEntities().values()) {
                if (entity2.getNetworkId() != this.getNetworkId() || n++ <= 10) continue;
                return true;
            }
            BaseEntity baseEntity2 = (BaseEntity)Entity.createEntity(this.getNetworkId(), (Position)this, new Object[0]);
            baseEntity2.setBaby(true);
            baseEntity2.spawnToAll();
            if (baseEntity2 instanceof EntityCow && player != null) {
                player.awardAchievement("breedCow");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (this.isKnockback() && entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
            return false;
        }
        if (this.fireProof && (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.LAVA || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.MAGMA)) {
            return false;
        }
        if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
            if (this instanceof EntityRavager && Utils.rand()) {
                ((EntityDamageByEntityEvent)entityDamageEvent).setKnockBack(0.0f);
            } else {
                ((EntityDamageByEntityEvent)entityDamageEvent).setKnockBack(0.25f);
            }
        }
        super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled()) {
            this.target = null;
            this.stayTime = 0;
        }
        return true;
    }

    @Override
    public boolean setMotion(Vector3 vector3) {
        if (this.getServer().getMobAiEnabled()) {
            super.setMotion(vector3);
        }
        return false;
    }

    @Override
    public boolean move(double d2, double d3, double d4) {
        AxisAlignedBB[] axisAlignedBBArray;
        if (d3 < -10.0 || d3 > 10.0) {
            if (!(this instanceof EntityFlyingMob)) {
                this.kill();
            }
            return false;
        }
        if (d2 == 0.0 && d4 == 0.0 && d3 == 0.0) {
            return false;
        }
        if (Timings.entityMoveTimer != null) {
            Timings.entityMoveTimer.startTiming();
        }
        this.blocksAround = null;
        double d5 = d2 * (double)this.moveMultiplier;
        double d6 = d3;
        double d7 = d4 * (double)this.moveMultiplier;
        for (AxisAlignedBB axisAlignedBB : axisAlignedBBArray = this.level.getCollisionCubes(this, this.boundingBox.addCoord(d2, d3, d4), false)) {
            d2 = axisAlignedBB.calculateXOffset(this.boundingBox, d2);
        }
        this.boundingBox.offset(d2, 0.0, 0.0);
        for (AxisAlignedBB axisAlignedBB : axisAlignedBBArray) {
            d4 = axisAlignedBB.calculateZOffset(this.boundingBox, d4);
        }
        this.boundingBox.offset(0.0, 0.0, d4);
        for (AxisAlignedBB axisAlignedBB : axisAlignedBBArray) {
            d3 = axisAlignedBB.calculateYOffset(this.boundingBox, d3);
        }
        this.boundingBox.offset(0.0, d3, 0.0);
        this.setComponents(this.x + d2, this.y + d3, this.z + d4);
        this.checkChunks();
        this.checkGroundState(d5, d6, d7, d2, d3, d4);
        this.updateFallState(this.onGround);
        if (Timings.entityMoveTimer != null) {
            Timings.entityMoveTimer.stopTiming();
        }
        return true;
    }

    @Override
    protected boolean applyNameTag(Player player, Item item) {
        if (item.getId() == 421 && item.hasCustomName() && !(this instanceof EntityEnderDragon)) {
            String string = item.getCustomName();
            this.namedTag.putString("CustomName", string);
            this.namedTag.putBoolean("CustomNameVisible", true);
            this.setNameTag(string);
            this.setNameTagVisible(true);
            return true;
        }
        return false;
    }

    public void setInLove() {
        this.setInLove(true);
    }

    public void setInLove(boolean bl) {
        if (bl) {
            if (!this.isBaby() && (this instanceof EntityAnimal || this instanceof EntityWolf)) {
                this.r = (short)600;
            }
        } else {
            this.r = 0;
        }
    }

    public boolean isInLove() {
        return this.r > 0;
    }

    public boolean isInLoveCooldown() {
        return this.q > 0;
    }

    protected Item[] getRandomArmor() {
        Item[] itemArray = new Item[4];
        Item item = Item.get(0);
        Item item2 = Item.get(0);
        Item item3 = Item.get(0);
        Item item4 = Item.get(0);
        switch (Utils.rand(1, 5)) {
            case 1: {
                if (Utils.rand(1, 100) >= 39 || Utils.rand(0, 1) != 0) break;
                item = Item.get(298, Utils.rand(30, 48), 1);
                break;
            }
            case 2: {
                if (Utils.rand(1, 100) >= 50 || Utils.rand(0, 1) != 0) break;
                item = Item.get(314, Utils.rand(40, 70), 1);
                break;
            }
            case 3: {
                if (Utils.rand(1, 100) >= 14 || Utils.rand(0, 1) != 0) break;
                item = Item.get(302, Utils.rand(100, 160), 1);
                break;
            }
            case 4: {
                if (Utils.rand(1, 100) >= 3 || Utils.rand(0, 1) != 0) break;
                item = Item.get(306, Utils.rand(100, 160), 1);
                break;
            }
            case 5: {
                if (Utils.rand(1, 100) != 100 || Utils.rand(0, 1) != 0) break;
                item = Item.get(310, Utils.rand(190, 256), 1);
            }
        }
        itemArray[0] = item;
        if (Utils.rand(1, 4) != 1) {
            switch (Utils.rand(1, 5)) {
                case 1: {
                    if (Utils.rand(1, 100) >= 39 || Utils.rand(0, 1) != 0) break;
                    item2 = Item.get(299, Utils.rand(60, 73), 1);
                    break;
                }
                case 2: {
                    if (Utils.rand(1, 100) >= 50 || Utils.rand(0, 1) != 0) break;
                    item2 = Item.get(315, Utils.rand(65, 105), 1);
                    break;
                }
                case 3: {
                    if (Utils.rand(1, 100) >= 14 || Utils.rand(0, 1) != 0) break;
                    item2 = Item.get(303, Utils.rand(170, 233), 1);
                    break;
                }
                case 4: {
                    if (Utils.rand(1, 100) >= 3 || Utils.rand(0, 1) != 0) break;
                    item2 = Item.get(307, Utils.rand(170, 233), 1);
                    break;
                }
                case 5: {
                    if (Utils.rand(1, 100) != 100 || Utils.rand(0, 1) != 0) break;
                    item2 = Item.get(311, Utils.rand(421, 521), 1);
                }
            }
        }
        itemArray[1] = item2;
        if (Utils.rand(1, 2) == 2) {
            switch (Utils.rand(1, 5)) {
                case 1: {
                    if (Utils.rand(1, 100) >= 39 || Utils.rand(0, 1) != 0) break;
                    item3 = Item.get(300, Utils.rand(35, 68), 1);
                    break;
                }
                case 2: {
                    if (Utils.rand(1, 100) >= 50 || Utils.rand(0, 1) != 0) break;
                    item3 = Item.get(316, Utils.rand(50, 98), 1);
                    break;
                }
                case 3: {
                    if (Utils.rand(1, 100) >= 14 || Utils.rand(0, 1) != 0) break;
                    item3 = Item.get(304, Utils.rand(170, 218), 1);
                    break;
                }
                case 4: {
                    if (Utils.rand(1, 100) >= 3 || Utils.rand(0, 1) != 0) break;
                    item3 = Item.get(308, Utils.rand(170, 218), 1);
                    break;
                }
                case 5: {
                    if (Utils.rand(1, 100) != 100 || Utils.rand(0, 1) != 0) break;
                    item3 = Item.get(312, Utils.rand(388, 488), 1);
                }
            }
        }
        itemArray[2] = item3;
        if (Utils.rand(1, 5) < 3) {
            switch (Utils.rand(1, 5)) {
                case 1: {
                    if (Utils.rand(1, 100) >= 39 || Utils.rand(0, 1) != 0) break;
                    item4 = Item.get(301, Utils.rand(35, 58), 1);
                    break;
                }
                case 2: {
                    if (Utils.rand(1, 100) >= 50 || Utils.rand(0, 1) != 0) break;
                    item4 = Item.get(317, Utils.rand(50, 86), 1);
                    break;
                }
                case 3: {
                    if (Utils.rand(1, 100) >= 14 || Utils.rand(0, 1) != 0) break;
                    item4 = Item.get(305, Utils.rand(100, 188), 1);
                    break;
                }
                case 4: {
                    if (Utils.rand(1, 100) >= 3 || Utils.rand(0, 1) != 0) break;
                    item4 = Item.get(309, Utils.rand(100, 188), 1);
                    break;
                }
                case 5: {
                    if (Utils.rand(1, 100) != 100 || Utils.rand(0, 1) != 0) break;
                    item4 = Item.get(313, Utils.rand(350, 428), 1);
                }
            }
        }
        itemArray[3] = item4;
        return itemArray;
    }

    protected void addArmorExtraHealth() {
        if (this.armor != null && this.armor.length == 4) {
            switch (this.armor[0].getId()) {
                case 298: {
                    this.a(1);
                    break;
                }
                case 302: 
                case 306: 
                case 314: {
                    this.a(2);
                    break;
                }
                case 310: {
                    this.a(3);
                }
            }
            switch (this.armor[1].getId()) {
                case 299: {
                    this.a(2);
                    break;
                }
                case 303: 
                case 307: 
                case 315: {
                    this.a(3);
                    break;
                }
                case 311: {
                    this.a(4);
                }
            }
            switch (this.armor[2].getId()) {
                case 300: {
                    this.a(1);
                    break;
                }
                case 304: 
                case 308: 
                case 316: {
                    this.a(2);
                    break;
                }
                case 312: {
                    this.a(3);
                }
            }
            switch (this.armor[3].getId()) {
                case 301: {
                    this.a(1);
                    break;
                }
                case 305: 
                case 309: 
                case 317: {
                    this.a(2);
                    break;
                }
                case 313: {
                    this.a(3);
                }
            }
        }
    }

    private void a(int n) {
        boolean bl = this.health == (float)this.maxHealth;
        this.maxHealth += n;
        if (bl) {
            this.setHealth(this.health + (float)n);
        }
    }

    public boolean canDespawn() {
        return Server.getInstance().despawnMobs;
    }

    public int nearbyDistanceMultiplier() {
        return 1;
    }

    @Override
    protected void checkGroundState(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (this.onGround && d2 == 0.0 && d3 == 0.0 && d4 == 0.0 && d5 == 0.0 && d6 == 0.0 && d7 == 0.0) {
            return;
        }
        this.isCollidedVertically = d3 != d6;
        this.isCollidedHorizontally = d2 != d5 || d4 != d7;
        this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
        this.onGround = d3 != d6 && d3 < 0.0;
    }

    public static void setProjectileMotion(EntityProjectile entityProjectile, double d2, double d3, double d4, double d5) {
        double d6 = Math.cos(d4);
        double d7 = d6 * Math.sin(-d3);
        double d8 = d6 * Math.cos(d3);
        double d9 = Math.sin(-FastMathLite.toRadians(d2));
        double d10 = Math.sqrt(d7 * d7 + d9 * d9 + d8 * d8);
        if (d10 > 0.0) {
            d7 += d7 * (d5 - d10) / d10;
            d9 += d9 * (d5 - d10) / d10;
            d8 += d8 * (d5 - d10) / d10;
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        entityProjectile.setMotion(new Vector3(d7 += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0, d9 += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0, d8 += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0));
    }

    public boolean canTarget(Entity entity) {
        return entity instanceof Player && entity.canBeFollowed();
    }

    @Override
    protected void checkBlockCollision() {
        for (Block block : this.getCollisionBlocks()) {
            block.onEntityCollide(this);
        }
    }

    protected float getArmorPoints(int n) {
        Float f2 = (Float)BaseEntity.n.get(n);
        if (f2 == null) {
            return 0.0f;
        }
        return f2.floatValue();
    }

    protected void playAttack() {
        EntityEventPacket entityEventPacket = new EntityEventPacket();
        entityEventPacket.eid = this.getId();
        entityEventPacket.event = 4;
        Server.broadcastPacket(this.getViewers().values(), (DataPacket)entityEventPacket);
    }

    protected boolean canSwimIn(int n) {
        return Block.hasWater(n);
    }
}


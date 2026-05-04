package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAnvil;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.entity.passive.EntityCow;
import cn.nukkit.entity.passive.EntityWolf;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;

/**
 * The base class of all entities that have an AI
 */
public abstract class BaseEntity extends EntityCreature implements EntityAgeable {

    public int stayTime;
    protected int moveTime;
    protected int noRotateTicks;

    protected float moveMultiplier = 1.0f;

    protected Vector3 target;
    protected Entity followTarget;
    protected boolean lookupForTarget = true;
    /**
     * Player who did last interact with this entity. Used to check who should get the cow breeding achievement.
     */
    protected Player lastInteract;
    protected int attackDelay;
    private long leadHolder = -1L;
    public long isAngryTo = -1L;
    private short inLoveTicks;
    private short inLoveCooldown;
    private boolean baby;
    private boolean friendly;
    private int lastDamageTick;
    @Setter
    @Getter
    private boolean persistent;
    private Boolean inTickingRangeCached;

    public Item[] armor;

    private static final Int2ObjectMap<Float> ARMOR_POINTS = new Int2ObjectOpenHashMap<Float>() {
        {
            put(Item.LEATHER_CAP, new Float(1));
            put(Item.LEATHER_TUNIC, new Float(3));
            put(Item.LEATHER_PANTS, new Float(2));
            put(Item.LEATHER_BOOTS, new Float(1));
            put(Item.CHAIN_HELMET, new Float(2));
            put(Item.CHAIN_CHESTPLATE, new Float(5));
            put(Item.CHAIN_LEGGINGS, new Float(4));
            put(Item.CHAIN_BOOTS, new Float(1));
            put(Item.GOLD_HELMET, new Float(2));
            put(Item.GOLD_CHESTPLATE, new Float(5));
            put(Item.GOLD_LEGGINGS, new Float(3));
            put(Item.GOLD_BOOTS, new Float(1));
            put(Item.IRON_HELMET, new Float(2));
            put(Item.IRON_CHESTPLATE, new Float(6));
            put(Item.IRON_LEGGINGS, new Float(5));
            put(Item.IRON_BOOTS, new Float(2));
            put(Item.DIAMOND_HELMET, new Float(3));
            put(Item.DIAMOND_CHESTPLATE, new Float(8));
            put(Item.DIAMOND_LEGGINGS, new Float(6));
            put(Item.DIAMOND_BOOTS, new Float(3));
            put(Item.NETHERITE_HELMET, new Float(3));
            put(Item.NETHERITE_CHESTPLATE, new Float(8));
            put(Item.NETHERITE_LEGGINGS, new Float(6));
            put(Item.NETHERITE_BOOTS, new Float(3));
            put(Item.TURTLE_SHELL, new Float(2));
        }
    };

    public BaseEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void setBaby(boolean baby) {
        this.baby = baby;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, baby);
        if (baby) {
            this.setScale(0.5f);
            this.age = Utils.rand(-2400, -1800);
        } else {
            this.setScale(1.0f);
        }
    }

    public void setFriendly(boolean bool) {
        this.friendly = bool;
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);

        if (server.vanillaBossBar && this instanceof EntityBoss) {
            for (Player p : this.getViewers().values()) {
                if (p.protocol < 361) {
                    continue;
                }

                BossEventPacket pkBoss = new BossEventPacket();
                pkBoss.bossEid = this.id;
                pkBoss.type = BossEventPacket.TYPE_HEALTH_PERCENT;
                pkBoss.title = this.getName();
                pkBoss.healthPercent = this.getHealth() / this.getRealMaxHealth();
                p.dataPacket(pkBoss);
            }
        }
    }

    public void setInLove(boolean inLove) {
        if (inLove) {
            if (!this.isBaby() && (this instanceof EntityAnimal || this instanceof EntityWolf)) {
                this.inLoveTicks = 600;
                //this.setDataFlag(DATA_FLAGS, DATA_FLAG_INLOVE, true); // Currently useless
            }
            this.setPersistent(true); // TODO: different flag for this?
        } else {
            this.inLoveTicks = 0;
            //this.setDataFlag(DATA_FLAGS, DATA_FLAG_INLOVE, false);
        }
    }

    public void setLookupForTarget(boolean lookupForTarget) {
        this.lookupForTarget = lookupForTarget;
    }

    public void setTarget(Entity target) {
        this.followTarget = target;
        this.moveTime = 0;
        this.stayTime = 0;
        this.target = null;
    }

    public void setTarget(Vector3 target) {
        if (target instanceof Entity) {
            this.setTarget((Entity) target);
            return;
        }

        this.followTarget = null;
        this.moveTime = 0;
        this.stayTime = 0;
        this.target = target;
    }

    public int getAge() {
        return this.age;
    }

    protected float getFreezingDamage() {
        return 1f;
    }

    public abstract int getKillExperience();

    /**
     * Modifier for mob knockback resistance
     *
     * @return 1 - vanilla resistance
     */
    protected float getKnockbackModifier() {
        return 1f;
    }

    /**
     * Get a random set of armor
     *
     * @return armor items
     */
    protected Item[] getRandomArmor() {
        Item[] slots = new Item[4];
        Item helmet = Item.get(0);
        Item chestplate = Item.get(0);
        Item leggings = Item.get(0);
        Item boots = Item.get(0);

        switch (Utils.rand(1, 5)) {
            case 1:
                if (Utils.rand(1, 100) < 39) {
                    if (Utils.rand(0, 1) == 0) {
                        helmet = Item.get(Item.LEATHER_HELMET, Utils.rand(30, 48), 1);
                    }
                }
                break;
            case 2:
                if (Utils.rand(1, 100) < 50) {
                    if (Utils.rand(0, 1) == 0) {
                        helmet = Item.get(Item.GOLD_HELMET, Utils.rand(40, 70), 1);
                    }
                }
                break;
            case 3:
                if (Utils.rand(1, 100) < 14) {
                    if (Utils.rand(0, 1) == 0) {
                        helmet = Item.get(Item.CHAIN_HELMET, Utils.rand(100, 160), 1);
                    }
                }
                break;
            case 4:
                if (Utils.rand(1, 100) < 3) {
                    if (Utils.rand(0, 1) == 0) {
                        helmet = Item.get(Item.IRON_HELMET, Utils.rand(100, 160), 1);
                    }
                }
                break;
            case 5:
                if (Utils.rand(1, 100) == 100) {
                    if (Utils.rand(0, 1) == 0) {
                        helmet = Item.get(Item.DIAMOND_HELMET, Utils.rand(190, 256), 1);
                    }
                }
                break;
        }

        slots[0] = helmet;

        if (Utils.rand(1, 4) != 1) {
            switch (Utils.rand(1, 5)) {
                case 1:
                    if (Utils.rand(1, 100) < 39) {
                        if (Utils.rand(0, 1) == 0) {
                            chestplate = Item.get(Item.LEATHER_CHESTPLATE, Utils.rand(60, 73), 1);
                        }
                    }
                    break;
                case 2:
                    if (Utils.rand(1, 100) < 50) {
                        if (Utils.rand(0, 1) == 0) {
                            chestplate = Item.get(Item.GOLD_CHESTPLATE, Utils.rand(65, 105), 1);
                        }
                    }
                    break;
                case 3:
                    if (Utils.rand(1, 100) < 14) {
                        if (Utils.rand(0, 1) == 0) {
                            chestplate = Item.get(Item.CHAIN_CHESTPLATE, Utils.rand(170, 233), 1);
                        }
                    }
                    break;
                case 4:
                    if (Utils.rand(1, 100) < 3) {
                        if (Utils.rand(0, 1) == 0) {
                            chestplate = Item.get(Item.IRON_CHESTPLATE, Utils.rand(170, 233), 1);
                        }
                    }
                    break;
                case 5:
                    if (Utils.rand(1, 100) == 100) {
                        if (Utils.rand(0, 1) == 0) {
                            chestplate = Item.get(Item.DIAMOND_CHESTPLATE, Utils.rand(421, 521), 1);
                        }
                    }
                    break;
            }
        }

        slots[1] = chestplate;

        if (Utils.rand(1, 2) == 2) {
            switch (Utils.rand(1, 5)) {
                case 1:
                    if (Utils.rand(1, 100) < 39) {
                        if (Utils.rand(0, 1) == 0) {
                            leggings = Item.get(Item.LEATHER_LEGGINGS, Utils.rand(35, 68), 1);
                        }
                    }
                    break;
                case 2:
                    if (Utils.rand(1, 100) < 50) {
                        if (Utils.rand(0, 1) == 0) {
                            leggings = Item.get(Item.GOLD_LEGGINGS, Utils.rand(50, 98), 1);
                        }
                    }
                    break;
                case 3:
                    if (Utils.rand(1, 100) < 14) {
                        if (Utils.rand(0, 1) == 0) {
                            leggings = Item.get(Item.CHAIN_LEGGINGS, Utils.rand(170, 218), 1);
                        }
                    }
                    break;
                case 4:
                    if (Utils.rand(1, 100) < 3) {
                        if (Utils.rand(0, 1) == 0) {
                            leggings = Item.get(Item.IRON_LEGGINGS, Utils.rand(170, 218), 1);
                        }
                    }
                    break;
                case 5:
                    if (Utils.rand(1, 100) == 100) {
                        if (Utils.rand(0, 1) == 0) {
                            leggings = Item.get(Item.DIAMOND_LEGGINGS, Utils.rand(388, 488), 1);
                        }
                    }
                    break;
            }
        }

        slots[2] = leggings;

        if (Utils.rand(1, 5) < 3) {
            switch (Utils.rand(1, 5)) {
                case 1:
                    if (Utils.rand(1, 100) < 39) {
                        if (Utils.rand(0, 1) == 0) {
                            boots = Item.get(Item.LEATHER_BOOTS, Utils.rand(35, 58), 1);
                        }
                    }
                    break;
                case 2:
                    if (Utils.rand(1, 100) < 50) {
                        if (Utils.rand(0, 1) == 0) {
                            boots = Item.get(Item.GOLD_BOOTS, Utils.rand(50, 86), 1);
                        }
                    }
                    break;
                case 3:
                    if (Utils.rand(1, 100) < 14) {
                        if (Utils.rand(0, 1) == 0) {
                            boots = Item.get(Item.CHAIN_BOOTS, Utils.rand(100, 188), 1);
                        }
                    }
                    break;
                case 4:
                    if (Utils.rand(1, 100) < 3) {
                        if (Utils.rand(0, 1) == 0) {
                            boots = Item.get(Item.IRON_BOOTS, Utils.rand(100, 188), 1);
                        }
                    }
                    break;
                case 5:
                    if (Utils.rand(1, 100) == 100) {
                        if (Utils.rand(0, 1) == 0) {
                            boots = Item.get(Item.DIAMOND_BOOTS, Utils.rand(350, 428), 1);
                        }
                    }
                    break;
            }
        }

        slots[3] = boots;

        return slots;
    }

    public double getSpeed() {
        if (this.baby) {
            return 1.2;
        }
        return 1;
    }

    public Vector3 getTarget() {
        return this.followTarget != null ? this.followTarget : this.target;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public boolean isFriendly() {
        return this.friendly;
    }

    public boolean isInLove() {
        return inLoveTicks > 0;
    }

    public boolean isInLoveCooldown() {
        return inLoveCooldown > 0;
    }

    protected boolean isInTickingRange() {
        return isInTickingRange(server.entityActivationRange);
    }

    public boolean isKnockback() {
        return this.knockBackTime > 0;
    }

    public boolean isLeashed() {
        return this.leadHolder != -1L;
    }

    public boolean isLookupForTarget() {
        return this.lookupForTarget;
    }

    /**
     * Increases mob's health according to armor the mob has (temporary workaround until armor damage modifiers are implemented for mobs)
     */
    protected void addArmorExtraHealth() {
        if (this.armor != null && this.armor.length == 4) {
            switch (armor[0].getId()) {
                case Item.LEATHER_HELMET:
                    this.addHealth(1);
                    break;
                case Item.GOLD_HELMET:
                case Item.CHAIN_HELMET:
                case Item.IRON_HELMET:
                    this.addHealth(2);
                    break;
                case Item.DIAMOND_HELMET:
                    this.addHealth(3);
                    break;
            }
            switch (armor[1].getId()) {
                case Item.LEATHER_CHESTPLATE:
                    this.addHealth(2);
                    break;
                case Item.GOLD_CHESTPLATE:
                case Item.CHAIN_CHESTPLATE:
                case Item.IRON_CHESTPLATE:
                    this.addHealth(3);
                    break;
                case Item.DIAMOND_CHESTPLATE:
                    this.addHealth(4);
                    break;
            }
            switch (armor[2].getId()) {
                case Item.LEATHER_LEGGINGS:
                    this.addHealth(1);
                    break;
                case Item.GOLD_LEGGINGS:
                case Item.CHAIN_LEGGINGS:
                case Item.IRON_LEGGINGS:
                    this.addHealth(2);
                    break;
                case Item.DIAMOND_LEGGINGS:
                    this.addHealth(3);
                    break;
            }
            switch (armor[3].getId()) {
                case Item.LEATHER_BOOTS:
                    this.addHealth(1);
                    break;
                case Item.GOLD_BOOTS:
                case Item.CHAIN_BOOTS:
                case Item.IRON_BOOTS:
                    this.addHealth(2);
                    break;
                case Item.DIAMOND_BOOTS:
                    this.addHealth(3);
                    break;
            }
        }
    }

    /**
     * Increase the maximum health and health. Used for armored mobs.
     *
     * @param health amount of health to add
     */
    private void addHealth(int health) {
        boolean wasMaxHealth = this.health == this.maxHealth;
        this.maxHealth = this.maxHealth + health;
        if (wasMaxHealth) {
            this.setHealth(this.health + health);
        }
    }

    @Override
    protected boolean applyNameTag(Player player, Item nameTag) {
        String name = nameTag.getCustomName();

        if (!name.isEmpty()) {
            this.namedTag.putString("CustomName", name);
            this.namedTag.putBoolean("CustomNameVisible", true);
            this.setNameTag(name);
            this.setNameTagVisible(true);
            return true; // onInteract: true = decrease count
        }

        return false;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (this.isKnockback() && source instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) source).getDamager() instanceof Player) {
            return false;
        }

        if (this.fireProof && (source.getCause() == EntityDamageEvent.DamageCause.FIRE || source.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || source.getCause() == EntityDamageEvent.DamageCause.LAVA || source.getCause() == EntityDamageEvent.DamageCause.MAGMA)) {
            return false;
        }

        if (source instanceof EntityDamageByEntityEvent) {
            ((EntityDamageByEntityEvent) source).setKnockBack(0.25f * getKnockbackModifier());
        }

        super.attack(source);

        if (!source.isCancelled()) {
            this.target = null;
            this.stayTime = 0;
            this.lastDamageTick = server.getTick();
        }

        return true;
    }

    /**
     * Check whether a mob is allowed to despawn
     *
     * @return can despawn
     */
    protected boolean canDespawn() {
        return this.y < -128 || (server.despawnMobs &&
                !this.persistent && this.age % 100 == 0 && this.riding == null && this.inLoveTicks <= 0 && this.inLoveCooldown <= 0 &&
                !this.isLeashed() && !this.hasCustomName() && server.getTick() - this.lastDamageTick > 600 && // no damage in 30 seconds
                !this.isInTickingRange(9216d) // 96 blocks
        );
    }

    protected boolean canSetTemporalTarget() {
        return this.followTarget == null;
    }

    /**
     * Override this to allow the mob to swim in lava
     *
     * @param block block id
     * @return can swim
     */
    protected boolean canSwimIn(int block) {
        return Block.isWater(block);
    }

    public boolean canTarget(Entity entity) {
        return entity instanceof Player && entity.canBeFollowed();
    }

    @Override
    protected void checkBlockCollision() {
        Block powderSnow = null;

        for (Block block : this.getCollisionBlocks()) {
            block.onEntityCollide(this);

            if (block.getId() == Block.POWDER_SNOW) {
                powderSnow = block;
            }
        }

        if (powderSnow != null) {
            this.inPowderSnowTicks++;

            if (this.getFreezingDamage() > 0 && this.inPowderSnowTicks >= 140 && server.getTick() % 40 == 0 && level.getGameRules().getBoolean(GameRule.FREEZE_DAMAGE)) {
                this.attack(new EntityDamageByBlockEvent(powderSnow, this, EntityDamageEvent.DamageCause.CONTACT, this.getFreezingDamage()));
            }
        } else if (this.inPowderSnowTicks != 0) {
            this.inPowderSnowTicks = 0;
        }

        // TODO: portals
    }

    @Override
    protected void checkGroundState(double movX, double movY, double movZ, double dx, double dy, double dz) {
        if (onGround && movX == 0 && movY == 0 && movZ == 0 && dx == 0 && dy == 0 && dz == 0) {
            return;
        }
        this.isCollidedVertically = movY != dy;
        this.isCollidedHorizontally = (movX != dx || movZ != dz);
        this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
        this.onGround = (movY != dy && movY < 0);
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        this.inTickingRangeCached = null;

        if (this.canDespawn()) {
            this.close();
            return false;
        }

        if (this instanceof EntityMob && this.attackDelay < 200) {
            this.attackDelay++;
        }

        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (this.moveTime > 0) {
            this.moveTime -= tickDiff;
        }

        if (this.noRotateTicks > 0) {
            this.noRotateTicks -= tickDiff;
        }

        if (this.isBaby() && this.age > 0) {
            this.setBaby(false);
        }

        if (this.isInLove()) {
            this.inLoveTicks -= tickDiff;
            // If DATA_FLAG_INLOVE ever used in setInLove(false), check if still in love again
            if (!this.isBaby() && this.age > 0 && this.age % 20 == 0) {
                for (int i = 0; i < 3; i++) {
                    this.level.addParticle(new HeartParticle(this.add(Utils.rand(-1.0, 1.0), this.getMountedYOffset() + Utils.rand(-1.0, 1.0), Utils.rand(-1.0, 1.0))));
                }
                Entity[] colliding = level.getCollidingEntities(this.boundingBox.grow(0.5f, 0.5f, 0.5f));
                for (Entity entity : colliding) {
                    if (entity != this && this.tryBreedWih(entity)) {
                        break;
                    }
                }
            }
        } else if (this.isInLoveCooldown()) {
            this.inLoveCooldown -= tickDiff;
        }

        if (this.y > this.highestPosition) {
            this.highestPosition = this.y;
        }

        return hasUpdate;
    }

    /**
     * Get armor defense points for item
     *
     * @param item item id
     * @return defense points
     */
    protected float getArmorPoints(int item) {
        Float points = ARMOR_POINTS.get(item);
        if (points == null) return 0;
        return points;
    }

    private boolean getSeenPercentOverZero(Vector3 source, Entity entity) {
        AxisAlignedBB bb = entity.getBoundingBox();

        if (bb.isVectorInside(source)) {
            return true;
        }

        double x = 1 / ((bb.getMaxX() - bb.getMinX()) * 2 + 1);
        double y = 1 / ((bb.getMaxY() - bb.getMinY()) * 2 + 1);
        double z = 1 / ((bb.getMaxZ() - bb.getMinZ()) * 2 + 1);

        double xOffset = (1 - Math.floor(1 / x) * x) / 2;
        double yOffset = (1 - Math.floor(1 / y) * y) / 2;
        double zOffset = (1 - Math.floor(1 / z) * z) / 2;

        for (double i = 0; i <= 1; i += x) {
            for (double j = 0; j <= 1; j += y) {
                for (double k = 0; k <= 1; k += z) {
                    Vector3 target = new Vector3(
                            bb.getMinX() + i * (bb.getMaxX() - bb.getMinX()) + xOffset,
                            bb.getMinY() + j * (bb.getMaxY() - bb.getMinY()) + yOffset,
                            bb.getMinZ() + k * (bb.getMaxZ() - bb.getMinZ()) + zOffset
                    );

                    if (!raycastHit(source, target, this.level, this.chunk)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean ignoredAsSaveReason() {
        return server.suomiCraftPEMode();
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.persistent = this.namedTag.getBoolean("Persistent");

        if (this.namedTag.getBoolean("Immobile")) {
            this.setImmobile();
        }

        if (this.namedTag.contains("Age")) {
            this.age = this.namedTag.getShort("Age");
        }

        if (this.namedTag.getBoolean("Baby")) {
            this.setBaby(true); // setScale here is incorrect if scale is saved
        }

        if (this.namedTag.contains("InLoveTicks")) {
            this.inLoveTicks = (short) this.namedTag.getShort("InLoveTicks");
        }

        if (this.namedTag.contains("InLoveCooldown")) {
            this.inLoveCooldown = (short) this.namedTag.getShort("InLoveCooldown");
        }
    }

    private boolean isInTickingRange(double rangeSquared) {
        if (this.inTickingRangeCached != null) {
            return this.inTickingRangeCached;
        }

        for (Player player : this.level.getPlayersList()) {
            // Ignore y so mobs won't stop falling into void unless movement behavior is tweaked for this
            double dx = player.x - this.x;
            double dz = player.z - this.z;
            if (dx * dx + dz * dz < rangeSquared) {
                return true;
            }
        }
        return false;
    }

    public void leash(Entity leadHolder) {
        this.leadHolder = leadHolder.getId();
        this.setDataProperty(new LongEntityData(DATA_LEAD_HOLDER_EID, this.leadHolder));
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_LEASHED, true);
    }

    @Override
    public boolean move(double dx, double dy, double dz) {
        if (dy < -10 || dy > 10) {
            if (!(this instanceof EntityFlyingMob)) {
                this.kill();
            }
            return false;
        }

        if (this.leadHolder != -1L) {
            Entity leadHolder = level.getEntity(this.leadHolder);

            if (leadHolder == null) {
                this.unleash();
            } else {
                double distance = this.distanceSquared(leadHolder);

                if (distance > 100) {
                    this.unleash();
                } else if (distance > 49) {
                    Vector3 toTarget = leadHolder.subtract(this).normalize();
                    toTarget.x *= 0.5;
                    toTarget.y *= 0.5;
                    toTarget.z *= 0.5;

                    this.setMotion(toTarget);

                    dx = toTarget.x;
                    dy = toTarget.y;
                    dz = toTarget.z;
                }
            }
        }

        if (dx == 0 && dz == 0 && dy == 0) {
            return false;
        }

        this.blocksAround = null;

        double movX = dx * moveMultiplier;
        double movY = dy;
        double movZ = dz * moveMultiplier;

        AxisAlignedBB[] list = this.level.getCollisionCubes(this, this.boundingBox.addCoord(dx, dy, dz), false);

        for (AxisAlignedBB bb : list) {
            dx = bb.calculateXOffset(this.boundingBox, dx);
        }
        this.boundingBox.offset(dx, 0, 0);

        for (AxisAlignedBB bb : list) {
            dz = bb.calculateZOffset(this.boundingBox, dz);
        }
        this.boundingBox.offset(0, 0, dz);

        for (AxisAlignedBB bb : list) {
            dy = bb.calculateYOffset(this.boundingBox, dy);
        }
        this.boundingBox.offset(0, dy, 0);

        this.setComponents(this.x + dx, this.y + dy, this.z + dz);
        this.checkChunks();

        this.checkGroundState(movX, movY, movZ, dx, dy, dz);
        this.updateFallState(this.onGround);

        return true;
    }

    /**
     * How near a player the mob should get before it starts attacking
     *
     * @return distance
     */
    protected int nearbyDistanceMultiplier() {
        return 1;
    }

    /**
     * Play attack animation to viewers
     */
    protected void playAttack() {
        EntityEventPacket pk = new EntityEventPacket();
        pk.eid = this.getId();
        pk.event = EntityEventPacket.ARM_SWING;
        Server.broadcastPacket(this.getViewers().values(), pk);
    }

    private static boolean raycastHit(Vector3 start, Vector3 end, Level level, FullChunk fullChunk) {
        Vector3 current = new Vector3(start.x, start.y, start.z);
        Vector3 direction = end.subtract(start).normalize();

        double stepX = NukkitMath.sign(direction.getX());
        double stepY = NukkitMath.sign(direction.getY());
        double stepZ = NukkitMath.sign(direction.getZ());

        double tMaxX = NukkitMath.boundary(start.getX(), direction.getX());
        double tMaxY = NukkitMath.boundary(start.getY(), direction.getY());
        double tMaxZ = NukkitMath.boundary(start.getZ(), direction.getZ());

        double tDeltaX = direction.getX() == 0 ? 0 : stepX / direction.getX();
        double tDeltaY = direction.getY() == 0 ? 0 : stepY / direction.getY();
        double tDeltaZ = direction.getZ() == 0 ? 0 : stepZ / direction.getZ();

        double radiusSquared = start.distanceSquared(end);

        while (true) {
            Block block = level.getBlock(fullChunk, NukkitMath.floorDouble(current.x), NukkitMath.floorDouble(current.y), NukkitMath.floorDouble(current.z), false);

            if ((block.isSolid() || block instanceof BlockAnvil) && block.calculateIntercept(current, end) != null) {
                return true;
            }

            if (tMaxX < tMaxY && tMaxX < tMaxZ) {
                if (tMaxX * tMaxX > radiusSquared) {
                    break;
                }

                current.x += stepX;
                tMaxX += tDeltaX;
            } else if (tMaxY < tMaxZ) {
                if (tMaxY * tMaxY > radiusSquared) {
                    break;
                }

                current.y += stepY;
                tMaxY += tDeltaY;
            } else {
                if (tMaxZ * tMaxZ > radiusSquared) {
                    break;
                }

                current.z += stepZ;
                tMaxZ += tDeltaZ;
            }
        }

        return false;
    }

    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("Baby", this.baby);
        this.namedTag.putBoolean("Persistent", this.isPersistent());
        this.namedTag.putBoolean("Immobile", this.isImmobile());
        this.namedTag.putShort("Age", this.age);
        this.namedTag.putShort("InLoveTicks", this.inLoveTicks);
        this.namedTag.putShort("InLoveCooldown", this.inLoveCooldown);
    }

    protected boolean seesTarget(Vector3 target) {
        if (target instanceof Entity) {
            Entity entity = (Entity) target;
            if (this.age % 2 == 0) {
                return this.getSeenPercentOverZero(this.add(0, 1.5, 0), entity);
            }
            return this.getSeenPercentOverZero(this.add(0, 0.5, 0), entity);
        }

        return true;
    }

    protected void sendHealthToRider() {
        for (Entity entity : this.passengers) {
            if (entity instanceof Player) {
                UpdateAttributesPacket pk = new UpdateAttributesPacket();
                int max = this.getMaxHealth();
                pk.entries = new Attribute[]{Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(max).setValue(this.health < max ? this.health : max)};
                pk.entityId = this.id;
                ((Player) entity).dataPacket(pk);
            }
        }
    }

    public void setInLove() {
        this.setInLove(true);
    }

    @Override
    public boolean setMotion(Vector3 motion) {
        if (this.getServer().getMobAiEnabled()) {
            super.setMotion(motion);
        }
        return false;
    }

    protected boolean shouldMobBurn() {
        if (this.closed || !this.isAlive()) {
            return false;
        }
        if (level.getDimension() != Level.DIMENSION_OVERWORLD || level.isRaining()) {
            return false;
        }
        if (this.fireTicks > 1 || this.age % (server.suomiCraftPEMode() ? 5 : 2) != 0) {
            return false;
        }
        int time = level.getTime() % Level.TIME_FULL;
        return (time < 12542 || time >= 23460) && !this.isInsideOfWater() && this.canSeeSky();
    }

    public boolean targetOption(EntityCreature creature, double distance) {
        if (this instanceof EntityMob) {
            if (creature instanceof Player) {
                Player player = (Player) creature;
                return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && distance <= 256;
            }
            return creature.isAlive() && !creature.closed && distance <= 256;
        } else if (this instanceof EntityAnimal && this.isInLove()) {
            return creature instanceof BaseEntity && ((BaseEntity) creature).isInLove() && creature.isAlive() && !creature.closed && creature.getNetworkId() == this.getNetworkId() && distance <= 256;
        }
        return false;
    }

    private boolean tryBreedWih(Entity entity) {
        if (entity instanceof BaseEntity && entity.getNetworkId() == this.getNetworkId()) {
            BaseEntity be = (BaseEntity) entity;
            if (be.isInLove() && !be.isBaby() && be.age > 0) {
                Player pl = be.lastInteract;
                be.lastInteract = null;
                this.setInLove(false);
                be.setInLove(false);
                this.inLoveCooldown = 1200;
                be.inLoveCooldown = 1200;
                this.stayTime = 60;
                be.stayTime = 60;
                int count = 0;
                for (Entity e : chunk.getEntities().values()) {
                    if (e.getNetworkId() == this.getNetworkId()) {
                        if (count++ > 10) return true; // return true so no more checks run
                    }
                }
                BaseEntity baby = (BaseEntity) Entity.createEntity(this.getNetworkId(), this);
                baby.setBaby(true);
                baby.setPersistent(true); // TODO: different flag for this?
                baby.spawnToAll();
                if (baby instanceof EntityCow) {
                    if (pl != null) {
                        pl.awardAchievement("breedCow");
                    }
                }
                this.level.dropExpOrb(this, Utils.rand(1, 7));
                return true;
            }
        }
        return false;
    }

    public void unleash() {
        this.leadHolder = -1L;
        this.setDataProperty(new LongEntityData(DATA_LEAD_HOLDER_EID, this.leadHolder));
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_LEASHED, false);
        this.level.dropItem(this.add(0, 0.5, 0), Item.get(ItemID.LEAD));

        EntityEventPacket pk = new EntityEventPacket();
        pk.eid = this.getId();
        pk.event = EntityEventPacket.REMOVE_LEASH;
        Server.broadcastPacket(this.hasSpawned.values(), pk);
    }

    public abstract Vector3 updateMove(int tickDiff);
}

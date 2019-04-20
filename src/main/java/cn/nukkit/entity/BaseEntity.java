package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EntityUtils;
import co.aikar.timings.Timings;

public abstract class BaseEntity extends EntityCreature implements EntityAgeable {

    protected int stayTime = 0;
    protected int moveTime = 0;

    public float maxJumpHeight = 1.2f;
    public double moveMultifier = 1.0d;

    protected Vector3 target = null;
    protected Entity followTarget = null;
    protected int attackDelay = 0;
    protected int inLoveTicks = 0;

    protected boolean baby = false;
    private boolean movement = true;
    private boolean friendly = false;

    public Item[] armor;

    private boolean despawn = Server.getInstance().getPropertyBoolean("entity-despawn-task", true);
    private int despawnTicks = Server.getInstance().getPropertyInt("ticks-per-entity-despawns", 8000);

    public BaseEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setHealth(this.getMaxHealth());
    }

    public abstract Vector3 updateMove(int tickDiff);

    public abstract int getKillExperience();

    public boolean isFriendly() {
        return this.friendly;
    }

    public boolean isMovement() {
        return this.getServer().getMobAiEnabled() && this.getServer().getOnlinePlayers().size() > 0 && this.movement;
    }

    public boolean isKnockback() {
        return this.attackTime > 0;
    }

    public void setFriendly(boolean bool) {
        this.friendly = bool;
    }

    public void setMovement(boolean value) {
        this.movement = value;
    }

    public double getSpeed() {
        if (this.isBaby()) {
            return 1.2;
        }
        return 1;
    }

    public float getMaxJumpHeight() {
        return this.maxJumpHeight;
    }

    public int getAge() {
        return this.age;
    }

    public Entity getTarget() {
        return this.followTarget != null ? this.followTarget : (this.target instanceof Entity ? (Entity) this.target : null);
    }

    public void setTarget(Entity target) {
        this.followTarget = target;
        this.moveTime = 0;
        this.stayTime = 0;
        this.target = null;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    @Override
    public void setBaby(boolean baby) {
        this.baby = baby;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, baby);
        this.setScale((float) 0.5);
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

        this.setDataProperty(new ByteEntityData(DATA_FLAG_NO_AI, (byte) 1));
    }

    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("Baby", this.isBaby());
        this.namedTag.putBoolean("Movement", this.isMovement());
        this.namedTag.putShort("Age", this.age);
    }

    public boolean targetOption(EntityCreature creature, double distance) {
        if (this instanceof EntityMob) {
            if (creature instanceof Player) {
                Player player = (Player) creature;
                return !player.closed && player.spawned && player.isAlive() && player.isSurvival() && distance <= 80;
            }
            return creature.isAlive() && !creature.closed && distance <= 80;
        }
        return false;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        Timings.entityBaseTickTimer.startTiming();

        if (this.despawn && this.age > this.despawnTicks && !this.hasCustomName() && !(this instanceof EntityBoss)) {
            this.close();
            return true;
        }

        if (this instanceof EntityMob && this.attackDelay < 400) {
            this.attackDelay++;
        }

        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (this.moveTime > 0) {
            this.moveTime -= tickDiff;
        }

        if (this.isInLove()) {
            this.inLoveTicks -= tickDiff;
            if (this.age % 20 == 0) {
                for (int i = 0; i < 3; i++) {
                    this.level.addParticle(new HeartParticle(this.add(EntityUtils.rand(-1.0, 1.0), this.getMountedYOffset() + EntityUtils.rand(-1.0, 1.0), EntityUtils.rand(-1.0, 1.0))));
                }
            }
        }

        Timings.entityBaseTickTimer.stopTiming();

        return hasUpdate;
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
            ((EntityDamageByEntityEvent) source).setKnockBack(0.25f);
        }

        super.attack(source);

        this.target = null;
        return true;
    }

    @Override
    public boolean setMotion(Vector3 motion) {
        if (this.getServer().getMobAiEnabled() && this.getServer().getOnlinePlayers().size() > 0) {
            super.setMotion(motion);
        }
        return true;
    }

    @Override
    public boolean move(double dx, double dy, double dz) {
        Timings.entityMoveTimer.startTiming();

        double movX = dx * moveMultifier;
        double movY = dy;
        double movZ = dz * moveMultifier;

        AxisAlignedBB[] list = this.level.getCollisionCubes(this, this.boundingBox.getOffsetBoundingBox(dx, dy, dz));
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

        Timings.entityMoveTimer.stopTiming();
        return true;
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (item.getId() == Item.NAME_TAG) {
            if (item.hasCustomName()) {
                this.setNameTag(item.getCustomName());
                this.setNameTagVisible(true);
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
                return true;
            }
        }

        return false;
    }

    @Override
    public Item[] getDrops() {
        if (this.hasCustomName()) {
            return new Item[]{Item.get(Item.NAME_TAG, 0, 1)};
        }
        return new Item[0];
    }

    public void setInLove() {
        this.inLoveTicks = 600;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_INLOVE);
    }

    public boolean isInLove() {
        return inLoveTicks > 0;
    }

    public Item[] getRandomArmor() {
        Item[] slots = new Item[4];
        Item helmet = new Item(0, 0, 0);
        Item chestplate = new Item(0, 0, 0);
        Item leggings = new Item(0, 0, 0);
        Item boots = new Item(0, 0, 0);

        switch (EntityUtils.rand(1, 5)) {
            case 1:
                if (EntityUtils.rand(1, 100) < 39) {
                    helmet = Item.get(Item.LEATHER_HELMET, 0, EntityUtils.rand(0, 1));
                }
                break;
            case 2:
                if (EntityUtils.rand(1, 100) < 50) {
                    helmet = Item.get(Item.GOLD_HELMET, 0, EntityUtils.rand(0, 1));
                }
                break;
            case 3:
                if (EntityUtils.rand(1, 100) < 14) {
                    helmet = Item.get(Item.CHAIN_HELMET, 0, EntityUtils.rand(0, 1));
                }
                break;
            case 4:
                if (EntityUtils.rand(1, 100) < 3) {
                    helmet = Item.get(Item.IRON_HELMET, 0, EntityUtils.rand(0, 1));
                }
                break;
            case 5:
                if (EntityUtils.rand(1, 100) == 100) {
                    helmet = Item.get(Item.DIAMOND_HELMET, 0, EntityUtils.rand(0, 1));
                }
                break;
        }

        slots[0] = helmet;

        if (EntityUtils.rand(1, 4) != 1) {
            switch (EntityUtils.rand(1, 5)) {
                case 1:
                    if (EntityUtils.rand(1, 100) < 39) {
                        chestplate = Item.get(Item.LEATHER_CHESTPLATE, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 2:
                    if (EntityUtils.rand(1, 100) < 50) {
                        chestplate = Item.get(Item.GOLD_CHESTPLATE, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 3:
                    if (EntityUtils.rand(1, 100) < 14) {
                        chestplate = Item.get(Item.CHAIN_CHESTPLATE, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 4:
                    if (EntityUtils.rand(1, 100) < 3) {
                        chestplate = Item.get(Item.IRON_CHESTPLATE, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 5:
                    if (EntityUtils.rand(1, 100) == 100) {
                        chestplate = Item.get(Item.DIAMOND_CHESTPLATE, 0, EntityUtils.rand(0, 1));
                    }
                    break;
            }
        }

        slots[1] = chestplate;

        if (EntityUtils.rand(1, 2) == 2) {
            switch (EntityUtils.rand(1, 5)) {
                case 1:
                    if (EntityUtils.rand(1, 100) < 39) {
                        leggings = Item.get(Item.LEATHER_LEGGINGS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 2:
                    if (EntityUtils.rand(1, 100) < 50) {
                        leggings = Item.get(Item.GOLD_LEGGINGS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 3:
                    if (EntityUtils.rand(1, 100) < 14) {
                        leggings = Item.get(Item.CHAIN_LEGGINGS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 4:
                    if (EntityUtils.rand(1, 100) < 3) {
                        leggings = Item.get(Item.IRON_LEGGINGS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 5:
                    if (EntityUtils.rand(1, 100) == 100) {
                        leggings = Item.get(Item.DIAMOND_LEGGINGS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
            }
        }

        slots[2] = leggings;

        if (EntityUtils.rand(1, 5) < 3) {
            switch (EntityUtils.rand(1, 5)) {
                case 1:
                    if (EntityUtils.rand(1, 100) < 39) {
                        boots = Item.get(Item.LEATHER_BOOTS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 2:
                    if (EntityUtils.rand(1, 100) < 50) {
                        boots = Item.get(Item.GOLD_BOOTS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 3:
                    if (EntityUtils.rand(1, 100) < 14) {
                        boots = Item.get(Item.CHAIN_BOOTS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 4:
                    if (EntityUtils.rand(1, 100) < 3) {
                        boots = Item.get(Item.IRON_BOOTS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
                case 5:
                    if (EntityUtils.rand(1, 100) == 100) {
                        boots = Item.get(Item.DIAMOND_BOOTS, 0, EntityUtils.rand(0, 1));
                    }
                    break;
            }
        }

        slots[3] = boots;

        return slots;
    }
}

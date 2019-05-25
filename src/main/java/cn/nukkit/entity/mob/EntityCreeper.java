package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.utils.EntityUtils;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.sound.TNTPrimeSound;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

import java.util.ArrayList;
import java.util.List;

public class EntityCreeper extends EntityWalkingMob implements EntityExplosive {

    public static final int NETWORK_ID = 33;

    private int bombTime = 0;

    public EntityCreeper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.7f;
    }

    @Override
    public double getSpeed() {
        return 0.9;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(20);
    }

    public int getBombTime() {
        return this.bombTime;
    }

    public void explode() {
        ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, this.isPowered() ? 3 : 2.8);
        this.server.getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this);

            if (ev.isBlockBreaking()) {
                explosion.explodeA();
            }

            explosion.explodeB();
        }

        this.close();
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.server.getDifficulty() < 1) {
            this.close();
            return false;
        }

        if (!this.isAlive()) {
            if (++this.deadTicks >= 23) {
                this.close();
                return false;
            }
            return true;
        }

        int tickDiff = currentTick - this.lastUpdate;
        this.lastUpdate = currentTick;
        this.entityBaseTick(tickDiff);

        if (!this.isMovement()) {
            return true;
        }

        if (this.isKnockback()) {
            this.move(this.motionX * tickDiff, this.motionY, this.motionZ * tickDiff);
            this.motionY -= this.getGravity() * tickDiff;
            this.updateMovement();
            return true;
        }

        Vector3 before = this.target;
        this.checkTarget();

        if (this.target instanceof EntityCreature || before != this.target) {
            double x = this.target.x - this.x;
            double z = this.target.z - this.z;

            double diff = Math.abs(x) + Math.abs(z);
            double distance = target.distance(this);
            if (distance <= 4) {
                if (target instanceof EntityCreature) {
                    if (bombTime == 0) {
                        this.level.addSound(new TNTPrimeSound(this.add(0, getEyeHeight())));
                        this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, true);
                    }
                    this.bombTime += tickDiff;
                    if (this.bombTime >= 30) {
                        this.explode();
                        return false;
                    }
                    if (distance <= 1) {
                        this.moveTime = 0;
                    }
                }
            } else {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, false);
                this.bombTime = 0;

                this.motionX = this.getSpeed() * 0.15 * (x / diff);
                this.motionZ = this.getSpeed() * 0.15 * (z / diff);
            }
            this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
        }

        double dx = this.motionX * tickDiff;
        double dz = this.motionZ * tickDiff;
        boolean isJump = this.checkJump(dx, dz);
        if (this.stayTime > 0) {
            this.stayTime -= tickDiff;
            this.move(0, this.motionY * tickDiff, 0);
        } else {
            Vector2 be = new Vector2(this.x + dx, this.z + dz);
            this.move(dx, this.motionY * tickDiff, dz);
            Vector2 af = new Vector2(this.x, this.z);

            if ((be.x != af.x || be.y != af.y) && !isJump) {
                this.moveTime -= 90 * tickDiff;
            }
        }

        if (!isJump) {
            if (this.onGround) {
                this.motionY = 0;
            } else if (this.motionY > -this.getGravity() * 4) {
                if (!(this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8),
                        NukkitMath.floorDouble(this.z))) instanceof BlockLiquid)) {
                    this.motionY -= this.getGravity() * 1;
                }
            } else {
                this.motionY -= this.getGravity() * tickDiff;
            }
        }
        this.updateMovement();
        return true;
    }

    public void attackEntity(Entity player) {}

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (this.hasCustomName()) {
            drops.add(Item.get(Item.NAME_TAG, 0, 1));
        }

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby()) {
            for (int i = 0; i < EntityUtils.rand(0, 2); i++) {
                drops.add(Item.get(Item.GUNPOWDER, 0, 1));
            }

            Entity killer = ((EntityDamageByEntityEvent) this.lastDamageCause).getDamager();

            if (killer instanceof EntitySkeleton || killer instanceof EntityStray) {
                drops.add(Item.get(EntityUtils.rand(500, 511), 0, 1));
            }

            if (killer instanceof EntityCreeper) {
                if (((EntityCreeper) killer).isPowered()) {
                    drops.add(Item.get(Item.SKULL, ItemSkull.CREEPER_HEAD, 1));
                }
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        super.onInteract(player, item);

        if (item.getId() == Item.FLINT_AND_STEEL) {
            level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_IGNITE);
            this.explode();

            return true;
        }

        return false;
    }

    public boolean isPowered() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_POWERED);
    }

    public void setPowered(boolean charged) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_POWERED, charged);
    }
}

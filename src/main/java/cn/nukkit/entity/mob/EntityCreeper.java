package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.sound.TNTPrimeSound;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

public class EntityCreeper extends EntityWalkingMob implements EntityExplosive {

    public static final int NETWORK_ID = 33;

    private short bombTime = 0;
    private boolean exploding;

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

        if (this.namedTag.contains("powered")) {
            this.setPowered(this.namedTag.getBoolean("powered"));
        }
    }

    public void explode() {
        if (this.closed) return;

        EntityExplosionPrimeEvent ev = new EntityExplosionPrimeEvent(this, this.isPowered() ? 6 : 3);
        this.server.getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this);

            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
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
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionY -= this.getGravity();
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
                    if (!exploding) {
                        if (bombTime == 0) {
                            this.level.addSound(new TNTPrimeSound(this.add(0, getEyeHeight())));
                            this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, true);
                        }
                        this.bombTime += tickDiff;
                        if (this.bombTime >= 30) {
                            this.explode();
                            return false;
                        }
                    }
                    if (distance <= 1) {
                        this.stayTime = 10;
                    }
                }
            } else {
                if (!exploding) {
                    this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, false);
                    this.bombTime = 0;
                }

                this.motionX = this.getSpeed() * 0.15 * (x / diff);
                this.motionZ = this.getSpeed() * 0.15 * (z / diff);
            }
            if (this.stayTime <= 0 || Utils.rand()) this.yaw = FastMath.toDegrees(-FastMath.atan2(x / diff, z / diff));
        }

        double dx = this.motionX;
        double dz = this.motionZ;
        boolean isJump = this.checkJump(dx, dz);
        if (this.stayTime > 0) {
            this.stayTime -= tickDiff;
            this.move(0, this.motionY, 0);
        } else {
            Vector2 be = new Vector2(this.x + dx, this.z + dz);
            this.move(dx, this.motionY, dz);
            Vector2 af = new Vector2(this.x, this.z);

            if ((be.x != af.x || be.y != af.y) && !isJump) {
                this.moveTime -= 90;
            }
        }

        if (!isJump) {
            if (this.onGround) {
                this.motionY = 0;
            } else if (this.motionY > -this.getGravity() * 4) {
                int b = this.level.getBlockIdAt(chunk, NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z));
                if (b != BlockID.WATER && b != BlockID.STILL_WATER) {
                    this.motionY -= this.getGravity();
                }
            } else {
                this.motionY -= this.getGravity();
            }
        }
        this.updateMovement();
        return true;
    }

    public void attackEntity(Entity player) {}

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        for (int i = 0; i < Utils.rand(0, 2); i++) {
            drops.add(Item.get(Item.GUNPOWDER, 0, 1));
        }

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            Entity killer = ((EntityDamageByEntityEvent) this.lastDamageCause).getDamager();

            if (killer instanceof EntitySkeleton || killer instanceof EntityStray) {
                drops.add(Item.get(Utils.rand(500, 511), 0, 1));
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
        return 5;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (item.getId() == Item.FLINT_AND_STEEL && !exploding) {
            this.exploding = true;
            level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_IGNITE);
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, true);
            level.addSound(new TNTPrimeSound(this.add(0, getEyeHeight())));
            level.getServer().getScheduler().scheduleDelayedTask(null, this::explode, 30);
            return true;
        }

        return super.onInteract(player, item, clickedPos);
    }

    public boolean isPowered() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_POWERED);
    }

    public void setPowered(boolean charged) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_POWERED, charged);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("powered", this.isPowered());
    }

    @Override
    public void onStruckByLightning(Entity entity) {
        if (this.attack(new EntityDamageByEntityEvent(entity, this, EntityDamageEvent.DamageCause.LIGHTNING, 5))) {
            if (this.fireTicks < 160) {
                this.setOnFire(8);
            }
            this.setPowered(true);
        }
    }
}

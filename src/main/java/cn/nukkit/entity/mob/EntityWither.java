package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.*;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.projectile.EntityBlueWitherSkull;
import cn.nukkit.entity.projectile.EntityWitherSkull;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Utils;
import org.apache.commons.math3.util.FastMath;

public class EntityWither extends EntityFlyingMob implements EntityBoss, EntitySmite {

    public static final int NETWORK_ID = 52;

    /**
        Whether the wither is exploded and dying
     */
    private boolean exploded;

    public EntityWither(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        return 3.5f;
    }

    @Override
    public double getSpeed() {
        return 1.3;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.fireProof = true;
        this.setMaxHealth(witherMaxHealth());
        this.setDamage(new int[]{0, 2, 4, 6});
        if (this.age == 0) {
            this.setDataProperty(new IntEntityData(DATA_WITHER_INVULNERABLE_TICKS, 200));
        }
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            if (!player.isSurvival() && !player.isAdventure()) {
                return false;
            }
        }
        return creature.isAlive() && !creature.closed && distance <= 256;
    }

    @Override
    public int getKillExperience() {
        return 50;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.age > 220 && this.attackDelay > 23 && Utils.rand(1, 5) < 3 && this.distanceSquared(player) <= 10000) {
            this.attackDelay = 0;

            double f = 1;
            double yaw = this.yaw + Utils.rand(-12.0, 12.0);
            double pitch = this.pitch + Utils.rand(-7.0, 7.0);
            Location pos = new Location(this.x - Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                    this.z + Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5, yaw, pitch, this.level);

            if (this.getLevel().getBlockIdAt((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()) != Block.AIR) {
                return;
            }

            Entity k;
            ProjectileLaunchEvent launch;
            EntityWitherSkull skull;
            if (Utils.rand(0, 200) > 180 || Utils.rand(0, 200) < 20) {
                f = 0.8;
                k = Entity.createEntity("BlueWitherSkull", pos, this);
                skull = (EntityBlueWitherSkull) k;
                ((EntityBlueWitherSkull) skull).setExplode(true);
                skull.setMotion(new Vector3(-Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f, -Math.sin(FastMath.toRadians(pitch)) * f * f,
                        Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f));
                launch = new ProjectileLaunchEvent(skull);
            } else {
                k = Entity.createEntity("WitherSkull", pos, this);
                skull = (EntityWitherSkull) k;
                skull.setMotion(new Vector3(-Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f, -Math.sin(FastMath.toRadians(pitch)) * f * f,
                        Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f));
                launch = new ProjectileLaunchEvent(skull);
            }

            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                skull.close();
            } else {
                skull.spawnToAll();
                this.level.addSoundToViewers(this, Sound.MOB_WITHER_SHOOT);
            }
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.NETHER_STAR, 0, 1)};
    }

    @Override
    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntity = new AddEntityPacket();
        addEntity.type = NETWORK_ID;
        addEntity.entityUniqueId = this.getId();
        addEntity.entityRuntimeId = this.getId();
        addEntity.yaw = (float) this.yaw;
        addEntity.headYaw = (float) this.yaw;
        addEntity.pitch = (float) this.pitch;
        addEntity.x = (float) this.x;
        addEntity.y = (float) this.y;
        addEntity.z = (float) this.z;
        addEntity.speedX = (float) this.motionX;
        addEntity.speedY = (float) this.motionY;
        addEntity.speedZ = (float) this.motionZ;
        addEntity.metadata = this.dataProperties.clone();
        addEntity.attributes = new Attribute[]{Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(witherMaxHealth()).setValue(witherMaxHealth())};
        return addEntity;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        if (this.age == 200) {
            this.explode();
            this.setDataProperty(new IntEntityData(DATA_WITHER_INVULNERABLE_TICKS, 0));
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 30;
    }

    @Override
    public void kill() {
        if (!this.isAlive()) {
            return;
        }

        if (!this.exploded && this.lastDamageCause != null && EntityDamageEvent.DamageCause.SUICIDE != this.lastDamageCause.getCause()) {
            this.exploded = true;
            this.explode();
        }

        super.kill();
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        if (this.age <= 200 && ev.getCause() != EntityDamageEvent.DamageCause.SUICIDE) {
            return false;
        }

        return super.attack(ev);
    }

    private int witherMaxHealth() {
        switch (this.getServer().getDifficulty()) {
            case 2:
                return 450;
            case 3:
                return 600;
            default:
                return 300;
        }
    }

    private void explode() {
        EntityExplosionPrimeEvent ev = new EntityExplosionPrimeEvent(this, 7);
        this.server.getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this);

            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }

            explosion.explodeB();
        }
    }
}

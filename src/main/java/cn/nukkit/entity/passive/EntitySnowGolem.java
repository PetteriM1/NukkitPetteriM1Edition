package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;
import org.apache.commons.math3.util.FastMath;

public class EntitySnowGolem extends EntityWalkingMob {

    public static final int NETWORK_ID = 21;

    public boolean sheared = false;

    public EntitySnowGolem(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setFriendly(true);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.7f;
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(4);

        if (this.namedTag.getBoolean("Sheared")) {
            this.shear(true);
        }
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return (!(creature instanceof Player) || creature.getId() == this.isAngryTo) && creature.isAlive() && distance <= 100;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && Utils.rand(1, 32) < 4 && this.distanceSquared(player) <= 55) {
            this.attackDelay = 0;

            double f = 1.2;
            double yaw = this.yaw + Utils.rand(-12.0, 12.0);
            double pitch = this.pitch + Utils.rand(-7.0, 7.0);
            Location location = new Location(this.x + (-Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5), this.y + this.getEyeHeight(),
                    this.z + (Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5), yaw, pitch, this.level);
            Entity k = Entity.createEntity("Snowball", location, this);
            if (k == null) {
                return;
            }

            EntitySnowball snowball = (EntitySnowball) k;
            snowball.setMotion(new Vector3(-Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f, -Math.sin(FastMath.toRadians(pitch)) * f * f, Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f).multiply(f));

            EntityShootBowEvent ev = new EntityShootBowEvent(this, Item.get(Item.ARROW, 0, 1), snowball, f);
            this.server.getPluginManager().callEvent(ev);

            EntityProjectile projectile = ev.getProjectile();
            if (ev.isCancelled()) {
                projectile.close();
            } else if (projectile != null) {
                ProjectileLaunchEvent launch = new ProjectileLaunchEvent(projectile);
                this.server.getPluginManager().callEvent(launch);
                if (launch.isCancelled()) {
                    projectile.close();
                } else {
                    projectile.spawnToAll();
                }
            }
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.SNOWBALL, 0, Utils.rand(0, 15))};
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Snow Golem";
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (this.age % 20 == 0 && (this.level.getDimension() == Level.DIMENSION_NETHER || (this.level.isRaining() && this.level.canBlockSeeSky(this)))) {
            this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.FIRE_TICK, 1));
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 10;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (item.getId() == Item.SHEARS && !this.sheared) {
            this.shear(true);
            this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_SHEAR);
            player.getInventory().getItemInHand().setDamage(item.getDamage() + 1);
            return true;
        }

        return super.onInteract(player, item, clickedPos);
    }

    public void shear(boolean shear) {
        this.sheared = shear;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SHEARED, shear);
    }

    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("Sheared", this.sheared);
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        if (super.attack(ev)) {
            if (ev instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) ev).getDamager() instanceof Player) {
                this.isAngryTo = ((EntityDamageByEntityEvent) ev).getDamager().getId();
            }
            return true;
        }

        return false;
    }
}

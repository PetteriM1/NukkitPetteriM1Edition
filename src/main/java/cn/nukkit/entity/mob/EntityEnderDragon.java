package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityBoss;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.item.EntityEndCrystal;
import cn.nukkit.entity.projectile.EntityEnderCharge;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.EntityUtils;

public class EntityEnderDragon extends EntityFlyingMob implements EntityBoss {

    public static final int NETWORK_ID = 53;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityEnderDragon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 13f;
    }

    @Override
    public float getHeight() {
        return 4f;
    }

    @Override
    public double getSpeed() {
        return 3;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(200);
    }

    @Override
    public int getKillExperience() {
        for (int i = 0; i < 167;) {
            this.level.dropExpOrb(this, 3);
            i++;
        }
        return 0;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && player.isSurvival() && distance <= 300 && distance > 20;
        }
        return creature.isAlive() && !creature.closed && distance <= 300 && distance > 20;
    }

    @Override
    public void attackEntity(Entity player) {
    if (this.attackDelay > 50 && EntityUtils.rand(1, 5) < 3 && this.distance(player) <= 300) {
            this.attackDelay = 0;

            double f = 1.1;
            double yaw = this.yaw + EntityUtils.rand(-220, 220) / 10;
            double pitch = this.pitch + EntityUtils.rand(-120, 120) / 10;
            Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                    this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
            Entity k = EntityUtils.create("EnderCharge", pos, this);
            if (!(k instanceof EntityEnderCharge)) {
                return;
            }

            EntityEnderCharge charge = (EntityEnderCharge) k;
            charge.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                    Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(charge);
            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                charge.kill();
            } else {
                charge.spawnToAll();
            }
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        for (Entity e : this.getLevel().getEntities()) {
            if (e instanceof EntityEndCrystal) {
                if (e.distanceSquared(this) <= 32) {
                    float health = this.getHealth();
                    if (!(health > this.getMaxHealth()) && health != 0) {
                        this.setHealth(health + 0.1f);
                    }
                }
            }
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public String getName() {
        return "Ender Dragon";
    }

    @Override
    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntity = new AddEntityPacket();
        addEntity.type = this.getNetworkId();
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
        addEntity.metadata = this.dataProperties;
        addEntity.attributes = new Attribute[]{Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(200).setValue(200)};
        return addEntity;
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        if (ev instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) ev).getDamager() instanceof EntityEnderCharge) {
                ev.setCancelled(true);
            }
        } else {
            super.attack(ev);
        }

        return true;
    }
}

package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.utils.EntityUtils;
import cn.nukkit.entity.projectile.EntityBlueWitherSkull;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;

public class EntityWither extends EntityFlyingMob {

    public static final int NETWORK_ID = 52;

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
        this.setHealth(600);
        this.setMaxHealth(600);
        this.setDamage(new int[]{0, 2, 4, 6});
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && player.isSurvival() && distance <= 100;
        }
        return creature.isAlive() && !creature.closed && distance <= 100;
    }

    @Override
    public int getKillExperience() {
        return 50;
    }

    @Override
    public void attackEntity(Entity player) {
    if (this.attackDelay > 20 && EntityUtils.rand(1, 5) < 3 && this.distance(player) <= 100) {
            this.attackDelay = 0;

            double f = 1;
            double yaw = this.yaw + EntityUtils.rand(-220, 220) / 10;
            double pitch = this.pitch + EntityUtils.rand(-120, 120) / 10;
            Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                    this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
            Entity k = EntityUtils.create("BlueWitherSkull", pos, this);
            if (!(k instanceof EntityBlueWitherSkull)) {
                return;
            }

            EntityBlueWitherSkull blueskull = (EntityBlueWitherSkull) k;
            blueskull.setExplode(true);
            blueskull.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                    Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(blueskull);
            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                blueskull.kill();
            } else {
                blueskull.spawnToAll();
                this.level.addSound(this, "mob.wither.shoot");
            }
        }
    }

    @Override
    public Item[] getDrops() {
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby()) {
            return new Item[]{Item.get(Item.NETHER_STAR, 0, 1)};
        } else {
            return new Item[0];
        }
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
        addEntity.attributes = new Attribute[]{Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(600).setValue(600)};
        return addEntity;
    }
}

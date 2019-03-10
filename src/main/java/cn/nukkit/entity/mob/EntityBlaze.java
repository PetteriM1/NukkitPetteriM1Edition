package cn.nukkit.entity.mob;

import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.entity.projectile.EntityBlazeFireBall;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityBlaze extends EntityFlyingMob {

    public static final int NETWORK_ID = 43;

    public EntityBlaze(FullChunk chunk, CompoundTag nbt) {
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
        return 1.8f;
    }

    @Override
    public float getGravity() {
        return 0.04f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.fireProof = true;
        this.setMaxHealth(20);
    }

    @Override
    protected void checkTarget() {
        if (this.isKnockback()) {
            return;
        }

        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive()) {
            return;
        }

        Vector3 target = this.target;
        if (!(target instanceof EntityCreature) || !this.targetOption((EntityCreature) target, this.distanceSquared(target))) {
            double near = Integer.MAX_VALUE;

            for (Entity entity : this.getLevel().getEntities()) {
                if (entity == this || !(entity instanceof EntityCreature) || entity instanceof EntityAnimal) {
                    continue;
                }

                EntityCreature creature = (EntityCreature) entity;
                if (creature instanceof BaseEntity && ((BaseEntity) creature).isFriendly() == this.isFriendly()) {
                    continue;
                }

                double distance = this.distanceSquared(creature);
                if (distance > near || !this.targetOption(creature, distance)) {
                    continue;
                }
                near = distance;

                this.moveTime = 0;
                this.target = creature;
            }
        }

        if (this.target instanceof EntityCreature && ((EntityCreature) this.target).isAlive()) {
            return;
        }

        int x, z;
        if (this.stayTime > 0) {
            if (EntityUtils.rand(1, 100) > 5) {
                return;
            }
            x = EntityUtils.rand(10, 30);
            z = EntityUtils.rand(10, 30);
            this.target = this.add(EntityUtils.rand() ? x : -x, EntityUtils.rand(-20, 20) / 10, EntityUtils.rand() ? z : -z);
        } else if (EntityUtils.rand(1, 410) == 1) {
            x = EntityUtils.rand(10, 30);
            z = EntityUtils.rand(10, 30);
            this.stayTime = EntityUtils.rand(100, 300);
            this.target = this.add(EntityUtils.rand() ? x : -x, EntityUtils.rand(-20, 20) / 10, EntityUtils.rand() ? z : -z);
        } else if (this.moveTime <= 0 || this.target == null) {
            x = EntityUtils.rand(20, 100);
            z = EntityUtils.rand(20, 100);
            this.stayTime = 0;
            this.moveTime = EntityUtils.rand(100, 200);
            this.target = this.add(EntityUtils.rand() ? x : -x, 0, EntityUtils.rand() ? z : -z);
        }
    }

    @Override
    public Vector3 updateMove(int tickDiff) {
        if (!this.isMovement()) {
            return null;
        }

        if (this.isKnockback()) {
            this.move(this.motionX * tickDiff, this.motionY * tickDiff, this.motionZ * tickDiff);
            this.updateMovement();
            return null;
        }

        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive()) {
            double x = this.followTarget.x - this.x;
            double y = this.followTarget.y - this.y;
            double z = this.followTarget.z - this.z;

            double diff = Math.abs(x) + Math.abs(z);
            if (this.stayTime > 0 || this.distance(this.followTarget) <= (this.getWidth() + 0.0d) / 2 + 0.05) {
                this.motionX = 0;
                this.motionZ = 0;
            } else {
                this.motionX = this.getSpeed() * 0.15 * (x / diff);
                this.motionZ = this.getSpeed() * 0.15 * (z / diff);
            }
            this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
            this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
            return this.followTarget;
        }

        Vector3 before = this.target;
        this.checkTarget();
        if (this.target instanceof EntityCreature || before != this.target) {
            double x = this.target.x - this.x;
            double y = this.target.y - this.y;
            double z = this.target.z - this.z;

            double diff = Math.abs(x) + Math.abs(z);
            double distance = this.distance(this.target);
            if (distance <= (this.getWidth() + 0.0d) / 2 + 0.05) {
                this.motionX = 0;
                this.motionZ = 0;
            } else {
                if (this.target instanceof EntityCreature) {
                    this.motionX = 0;
                    this.motionZ = 0;
                    if (distance > this.y - this.getLevel().getHighestBlockAt((int) this.x, (int) this.z)) {
                        this.motionY = this.getGravity();
                    } else {
                        this.motionY = 0;
                    }
                } else {
                    this.motionX = this.getSpeed() * 0.15 * (x / diff);
                    this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                }
            }
            this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
            this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
        }

        double dx = this.motionX * tickDiff;
        double dz = this.motionZ * tickDiff;
        if (this.stayTime > 0) {
            this.stayTime -= tickDiff;
            this.move(0, this.motionY * tickDiff, 0);
        } else {
            this.move(dx, this.motionY * tickDiff, dz);
        }

        this.updateMovement();
        return this.target;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 20 && EntityUtils.rand(1, 32) < 4 && this.distance(player) <= 100) {
            this.attackDelay = 0;

            double f = 1.1;
            double yaw = this.yaw + EntityUtils.rand(-150, 150) / 10;
            double pitch = this.pitch + EntityUtils.rand(-75, 75) / 10;
            Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                    this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
            Entity k = EntityUtils.create("BlazeFireBall", pos, this);
            if (!(k instanceof EntityBlazeFireBall)) {
                return;
            }

            EntityBlazeFireBall fireball = (EntityBlazeFireBall) k;
            fireball.setExplode(true);
            fireball.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                    Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(fireball);
            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                fireball.kill();
            } else {
                fireball.spawnToAll();
                this.level.addLevelEvent(this, LevelEventPacket.EVENT_SOUND_BLAZE_SHOOT);
            }
        }
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (this.hasCustomName()) {
            drops.add(Item.get(Item.NAME_TAG, 0, 1));
        }

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby()) {
            for (int i = 0; i < EntityUtils.rand(0, 2); i++) {
                drops.add(Item.get(Item.BLAZE_ROD, 0, 1));
            }

            for (int i = 0; i < EntityUtils.rand(0, 3); i++) {
                drops.add(Item.get(Item.GLOWSTONE_DUST, 0, 1));
            }
        }

        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 10;
    }
}

package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.WalkingMonster;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.sound.LaunchSound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

public class EntityShulker extends WalkingMonster {

    public static final int NETWORK_ID = 54;

    public EntityShulker(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return "Shulker";
    }

    @Override
    public float getWidth() {
        return 1f;
    }

    @Override
    public float getHeight() {
        return 1f;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.fireProof = true;
        setMaxHealth(15);
    }

    @Override
    public void attackEntity(Entity player) {
    /*if (this.attackDelay > 23 && Utils.rand(1, 32) < 4 && this.distanceSquared(player) <= 55) {
            this.attackDelay = 0;

            double f = 1.2;
            double yaw = this.yaw + Utils.rand(-220, 220) / 10;
            double pitch = this.pitch + Utils.rand(-120, 120) / 10;
            Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getHeight() - 0.18,
                    this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
            Entity k = Utils.create("ShulkerBullet", pos, this);
            if (!(k instanceof ShulkerBullet)) {
                return;
            }

            ShulkerBullet arrow = (ShulkerBullet) k;
            arrow.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                    Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(arrow);
            this.server.getPluginManager().callEvent(launch);
            arrow.spawnToAll();
            this.level.addSound(new LaunchSound(this), this.getViewers().values());
        }*/
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = this.getNetworkId();
        pk.entityUniqueId = this.getId();
        pk.entityRuntimeId = this.getId();
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }
}

package cn.nukkit.entity.mob;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityBlazeFireBall;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.Utils;
import org.apache.commons.math3.util.FastMath;

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
    public void initEntity() {
        super.initEntity();

        this.fireProof = true;
        this.setMaxHealth(20);
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && Utils.rand(1, 32) < 4 && this.distanceSquared(player) <= 10000) {
            this.attackDelay = 0;

            double f = 1.1;
            double yaw = this.yaw + Utils.rand(-8.0, 8.0);
            double pitch = this.pitch + Utils.rand(-5.0, 5.0);
            Location pos = new Location(this.x - Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                    this.z + Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5, yaw, pitch, this.level);

            if (this.getLevel().getBlockIdAt((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()) != Block.AIR) {
                return;
            }

            EntityBlazeFireBall fireball = (EntityBlazeFireBall) Entity.createEntity("BlazeFireBall", pos, this);

            fireball.setMotion(new Vector3(-Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f, -Math.sin(FastMath.toRadians(pitch)) * f * f,
                    Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(fireball);
            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                fireball.close();
            } else {
                fireball.spawnToAll();
                this.level.addLevelEvent(this, LevelEventPacket.EVENT_SOUND_BLAZE_SHOOT);
            }
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.BLAZE_ROD, 0, Utils.rand(0, 1))};
    }

    @Override
    public int getKillExperience() {
        return 10;
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 30;
    }
}

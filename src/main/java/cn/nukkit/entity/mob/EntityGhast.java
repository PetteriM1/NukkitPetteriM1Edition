package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.projectile.EntityGhastFireBall;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.Utils;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

public class EntityGhast extends EntityFlyingMob {

    public static final int NETWORK_ID = 41;

    public EntityGhast(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 4;
    }

    @Override
    public float getHeight() {
        return 4;
    }

    @Override
    public double getSpeed() {
        return 1.2;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.fireProof = true;
        this.setMaxHealth(10);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && (player.isSurvival() || player.isAdventure()) && distance <= 256;
        }
        return creature.isAlive() && !creature.closed && distance <= 256;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 60 && Utils.rand(1, 32) < 4 && this.distanceSquared(player) <= 10000) {
            this.attackDelay = 0;

            double f = 1;
            double yaw = this.yaw + Utils.rand(-8.0, 8.0);
            double pitch = this.pitch + Utils.rand(-5.0, 5.0);
            Location pos = new Location(this.x - Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                    this.z + Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5, yaw, pitch, this.level);

            if (this.getLevel().getBlockIdAt((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()) != Block.AIR) {
                return;
            }

            EntityGhastFireBall fireball = (EntityGhastFireBall) Entity.createEntity("GhastFireBall", pos, this);
            fireball.setExplode(true);
            fireball.setMotion(new Vector3(-Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f, -Math.sin(FastMath.toRadians(pitch)) * f * f,
                    Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(fireball);
            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                fireball.close();
            } else {
                fireball.spawnToAll();
                this.level.addLevelEvent(this, LevelEventPacket.EVENT_SOUND_GHAST_SHOOT);
            }
        }
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        for (int i = 0; i < Utils.rand(0, 2); i++) {
            drops.add(Item.get(Item.GUNPOWDER, 0, 1));
        }

        drops.add(Item.get(Item.GHAST_TEAR, 0, Utils.rand(0, 1)));

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 30;
    }
}

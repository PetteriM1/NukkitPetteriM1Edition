package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.projectile.EntityBlazeFireBall;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.Utils;

public class EntityBlaze extends EntityFlyingMob {

    public static final int NETWORK_ID = 43;

    private int fireball;

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
        this.setMaxHealth(20);
        super.initEntity();
        this.fireProof = true;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && distance <= 2304; // 48 blocks
        }
        return false;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay == 60 && this.fireball == 0) {
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHARGED, true);
        }
        if (((this.fireball > 0 && this.fireball < 3 && this.attackDelay > 5) || (this.attackDelay > 120 && Utils.rand(1, 32) < 4)) && this.distanceSquared(player) <= 256) { // 16 blocks
            this.attackDelay = 0;
            this.fireball++;

            if (this.fireball == 3) {
                this.fireball = 0;
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHARGED, false);
            }

            EntityBlazeFireBall shot = (EntityBlazeFireBall) Entity.createEntity("BlazeFireBall", this.add(0, this.getEyeHeight(), 0), this);

            if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                shot.close();
                return;
            }

            shot.setMotion(player.add(0, 0.3, 0).subtract(this).normalize().multiply(1.2));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(shot);
            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                shot.close();
            } else {
                shot.spawnToAll();
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
    protected int nearbyDistanceMultiplier() {
        return target instanceof EntityLiving || followTarget instanceof EntityLiving ? 1000 : 1; // don't follow
    }
}

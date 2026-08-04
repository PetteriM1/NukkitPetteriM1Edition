package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.projectile.EntityGhastFireBall;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityGhast extends EntityFlyingMob {

    public static final int NETWORK_ID = 41;

    private boolean attacked;

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
        this.setMaxHealth(10);
        super.initEntity();

        this.fireProof = true;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_FIRE_IMMUNE, true);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && distance <= (this.attacked ? 4096 : 784);
        }
        return false;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.distanceSquared(player) <= (this.attacked ? 4096 : 784)) { // 28 blocks or 64 blocks if attacked)
            if (Utils.rand()) {
                this.attackDelay--;
                return;
            }
            if (this.attackDelay == 50) {
                this.setDataProperty(new ByteEntityData(Entity.DATA_CHARGE_AMOUNT, 1));
                this.level.addLevelEvent(this, LevelEventPacket.EVENT_SOUND_GHAST);
            }
            if (this.attackDelay > 60) {
                this.attackDelay = 0;
                this.setDataProperty(new ByteEntityData(Entity.DATA_CHARGE_AMOUNT, 0));

                EntityGhastFireBall shot = (EntityGhastFireBall) Entity.createEntity("GhastFireBall", this.add(0, this.getEyeHeight() - 1, 0), this);

                if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                    shot.close();
                    return;
                }

                shot.setMotion(player.subtract(this).normalize().multiply(1.1));

                ProjectileLaunchEvent launch = new ProjectileLaunchEvent(shot);
                this.server.getPluginManager().callEvent(launch);
                if (launch.isCancelled()) {
                    shot.close();
                } else {
                    shot.spawnToAll();
                    this.level.addLevelEvent(this, LevelEventPacket.EVENT_SOUND_GHAST_SHOOT);
                }
            }
        }
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        boolean result = super.attack(ev);

        if (!ev.isCancelled() && ev instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) ev).getDamager() instanceof Player) {
                this.attacked = true;
            }
        }

        return result;
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
    protected int nearbyDistanceMultiplier() {
        return target instanceof EntityLiving || followTarget instanceof EntityLiving ? 1000 : 1; // don't follow
    }

    @Override
    public void kill() {
        if (this.isAlive()) {
            super.kill();

            if (this.getLastDamageCause() instanceof EntityDamageByChildEntityEvent && ((EntityDamageByChildEntityEvent) this.getLastDamageCause()).getDamager() == this) {
                Entity damager = ((EntityDamageByChildEntityEvent) this.getLastDamageCause()).getChild();
                if (damager instanceof EntityGhastFireBall && ((EntityGhastFireBall) damager).directionChanged != null) {
                    ((EntityGhastFireBall) damager).directionChanged.awardAchievement("ghast");
                }
            }
        }
    }
}

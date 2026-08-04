package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityPillager extends EntityWalkingMob {

    public static final int NETWORK_ID = 114;

    private boolean angryFlagSet;

    public EntityPillager(FullChunk chunk, CompoundTag nbt) {
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
        return 1.9f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(24);
        super.initEntity();
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 60 && this.distanceSquared(player) <= 64) { // 8 blocks
            if (!this.seesTarget(player)) {
                return;
            }

            this.attackDelay = 0;

            EntityArrow shot = (EntityArrow) Entity.createEntity("Arrow", this.add(0, this.getEyeHeight(), 0), this);

            if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                shot.close();
                return;
            }

            EntityShootBowEvent ev = new EntityShootBowEvent(this, Item.get(Item.ARROW, 0, 1), shot, 2);
            this.server.getPluginManager().callEvent(ev);

            shot.setMotion(player.add(Utils.rand(-0.1, 0.1), Utils.rand(-0.1, 0.1) + player.getEyeHeight(), Utils.rand(-0.1, 0.1)).subtract(this).normalize().multiply(ev.getForce()));

            EntityProjectile projectile = ev.getProjectile();
            if (ev.isCancelled()) {
                projectile.close();
            } else {
                ProjectileLaunchEvent launch = new ProjectileLaunchEvent(projectile);
                this.server.getPluginManager().callEvent(launch);
                if (launch.isCancelled()) {
                    projectile.close();
                } else {
                    projectile.namedTag.putDouble("damage", 4);
                    projectile.updateRotation();
                    projectile.spawnToAll();
                    ((EntityArrow) projectile).setPickupMode(EntityArrow.PICKUP_NONE);
                    this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_CROSSBOW_SHOOT);
                }
            }
        }
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        for (int i = 0; i < Utils.rand(0, 2); i++) {
            drops.add(Item.get(Item.ARROW, 0, 1));
        }

        if (Utils.rand(1, 12) == 1) {
            drops.add(Item.get(Item.CROSSBOW, Utils.rand(300, 380), Utils.rand(0, 1)));
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = Item.get(Item.CROSSBOW, 0, 1);
        pk.hotbarSlot = 0;
        player.dataPacket(pk);
    }

    @Override
    protected int nearbyDistanceMultiplier() {
        return target instanceof EntityLiving || followTarget instanceof EntityLiving ? 20 : 1;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        boolean hasTarget = super.targetOption(creature, distance);
        if (hasTarget) {
            if (!this.angryFlagSet) {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHARGED, true);
                this.angryFlagSet = true;
            }
        } else {
            if (this.angryFlagSet) {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHARGED, false);
                this.angryFlagSet = false;
                this.stayTime = 100;
            }
        }
        return hasTarget;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }
}

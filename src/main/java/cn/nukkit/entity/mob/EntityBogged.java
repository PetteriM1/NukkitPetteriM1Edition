package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector2;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityBogged extends EntityWalkingMob implements EntitySmite {

    public static final int NETWORK_ID = 144;

    private boolean angryFlagSet;

    public EntityBogged(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > (server.getDifficulty() == 3 ? 50 : 70) && this.distanceSquared(player) <= 225) { // 15 blocks
            if (!this.seesTarget(player)) {
                return;
            }

            this.attackDelay = 0;

            EntityArrow shot = (EntityArrow) Entity.createEntity("Arrow", this.add(0, this.getEyeHeight(), 0), this);

            if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                shot.close();
                return;
            }

            shot.setData(26); // Poison arrow

            EntityShootBowEvent ev = new EntityShootBowEvent(this, Item.get(Item.ARROW, 0, 1), shot, 1.5);
            this.server.getPluginManager().callEvent(ev);

            shot.setMotion(player.add(Utils.rand(-0.1, 0.1), Utils.rand(-0.1, 0.1) + 0.3, Utils.rand(-0.1, 0.1)).subtract(this).normalize().multiply(ev.getForce()));

            EntityProjectile projectile = ev.getProjectile();
            if (ev.isCancelled()) {
                projectile.close();
            } else {
                ProjectileLaunchEvent launch = new ProjectileLaunchEvent(projectile);
                this.server.getPluginManager().callEvent(launch);
                if (launch.isCancelled()) {
                    projectile.close();
                } else {
                    projectile.updateRotation();
                    projectile.spawnToAll();
                    ((EntityArrow) projectile).setPickupMode(EntityArrow.PICKUP_NONE);
                    this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BOW);
                }
            }
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate;

        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        hasUpdate = super.entityBaseTick(tickDiff);

        if (shouldMobBurn()) {
            this.setOnFire(100);
        }

        return hasUpdate;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        for (int i = 0; i < Utils.rand(0, 2); i++) {
            drops.add(Item.get(Item.BONE, 0, 1));
        }

        for (int i = 0; i < Utils.rand(0, 2); i++) {
            drops.add(Item.get(Item.ARROW, 0, 1));
        }

        if (Utils.rand()) {
            drops.add(Item.get(Item.ARROW, 26, 1));
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }

    @Override
    public int getKillExperience() {
        return 5;
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
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
    }

    @Override
    public void kill() {
        if (!this.isAlive()) {
            return;
        }

        super.kill();

        if (this.lastDamageCause instanceof EntityDamageByChildEntityEvent) {
            Entity damager;
            if (((EntityDamageByChildEntityEvent) this.lastDamageCause).getChild() instanceof EntityArrow && (damager = ((EntityDamageByChildEntityEvent) this.lastDamageCause).getDamager()) instanceof Player) {
                if (new Vector2(this.x, this.z).distance(new Vector2(damager.x, damager.z)) >= 50) {
                    ((Player) damager).awardAchievement("snipeSkeleton");
                }
            }
        }
    }

    @Override
    protected int nearbyDistanceMultiplier() {
        return target instanceof EntityLiving || followTarget instanceof EntityLiving ? 8 : 1;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = Item.get(Item.BOW);
        pk.hotbarSlot = 0;
        player.dataPacket(pk);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        boolean hasTarget = super.targetOption(creature, distance);
        if (hasTarget) {
            if (!this.angryFlagSet && creature != null) {
                this.setDataProperty(new LongEntityData(DATA_TARGET_EID, creature.getId()));
                this.angryFlagSet = true;
            }
        } else {
            if (this.angryFlagSet) {
                this.setDataProperty(new LongEntityData(DATA_TARGET_EID, 0));
                this.angryFlagSet = false;
                this.stayTime = 100;
            }
        }
        return hasTarget;
    }
}

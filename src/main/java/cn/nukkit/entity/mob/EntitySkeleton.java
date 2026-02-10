package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.*;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBow;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector2;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EntitySkeleton extends EntityWalkingMob implements EntitySmite, EntityMobWithTool {

    public static final int NETWORK_ID = 34;

    private boolean angryFlagSet;

    private Item tool;
    private Item offhand;

    public EntitySkeleton(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
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

        if (this.getTool() instanceof ItemBow && ThreadLocalRandom.current().nextInt(1000) < 85) {
            drops.add(Item.get(Item.BOW, Utils.rand(100, 380), 1));
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
    public Item getOffhand() {
        return this.offhand;
    }

    @Override
    public Item getTool() {
        return this.tool;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public void setOffhand(Item offhand) {
        this.offhand = offhand;
    }

    @Override
    public void setTool(Item tool) {
        this.tool = tool;
    }

    @Override
    public void attackEntity(Entity player) {
        if (!(this.tool instanceof ItemBow)) {
            return; // TODO
        }

        double distance = this.distanceSquared(player);
        double delay = server.getDifficulty() == 3 ? 20 + (distance / 5.63) : 60;
        if (this instanceof EntityBogged || this instanceof EntityParched) {
            delay += 30;
        }

        if (this.attackDelay > delay && distance <= 225) { // 15 blocks
            if (!this.seesTarget(player)) {
                return;
            }

            this.attackDelay = 0;

            EntityArrow shot = (EntityArrow) Entity.createEntity("Arrow", this.add(0, this.getEyeHeight(), 0), this);

            if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                shot.close();
                return;
            }

            if (this instanceof EntityStray) {
                shot.setData(19); // Slowness arrow
            } else if (this instanceof EntityBogged) {
                shot.setData(26); // Poison arrow
            } else if (this instanceof EntityParched) {
                shot.setData(36); // Weakness arrow
            }

            EntityShootBowEvent ev = new EntityShootBowEvent(this, Item.get(Item.ARROW, 0, 1), shot, 1.7);
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
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();

        if (this.namedTag.contains("Item")) {
            this.tool = NBTIO.getItemHelper(this.namedTag.getCompound("Item"));
        } else {
            this.tool = Item.get(Item.BOW);
        }
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
                if (new Vector2(this.x, this.z).distanceSquared(damager.x, damager.z) >= 2500) { // 50 blocks
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
    public void saveNBT() {
        super.saveNBT();

        if (this.tool != null) {
            this.namedTag.put("Item", NBTIO.putItemHelper(this.tool));
        } else {
            this.namedTag.remove("Item");
        }
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        this.sendHandItems(player);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        boolean hasTarget = targetOptionInternal(creature, distance);
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

    private boolean targetOptionInternal(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            if (!player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure())) {
                PlayerInventory inv = player.getInventory();
                Item helmet;
                if (inv != null && (helmet = inv.getHelmetFast()).getId() == Item.SKULL && helmet.getDamage() == ItemSkull.SKELETON_SKULL) {
                    return distance <= 64;
                }
                return distance <= 256;
            }
            return false;
        }
        return creature.isAlive() && !creature.closed && distance <= 256;
    }
}

package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;
import cn.nukkit.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityWitch extends EntityWalkingMob {

    public static final int NETWORK_ID = 45;

    public EntityWitch(FullChunk chunk, CompoundTag nbt) {
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
        return 1.95f;
    }

    @Override
    public double getSpeed() {
        return 1.0;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.setMaxHealth(26);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return !player.closed && player.spawned && player.isAlive() && player.isSurvival() && distance <= 80;
        }
        return creature.isAlive() && !creature.closed && distance <= 80;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.getServer().getMobAiEnabled()) {
            if (this.attackDelay > 60 && this.distanceSquared(player) <= 20) {
                this.attackDelay = 0;
                if (player.isAlive() && !player.closed) {

                    double f = 1;
                    double yaw = this.yaw + EntityUtils.rand(-150.0, 150.0) / 10;
                    double pitch = this.pitch + EntityUtils.rand(-75.0, 75.0) / 10;
                    Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                            this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);

                    EntityPotion thrownPotion = (EntityPotion) EntityUtils.create("ThrownPotion", pos, this);

                    if (this.distance(player) <= 8 && !player.hasEffect(Effect.SLOWNESS)) {
                        thrownPotion.potionId = Potion.SLOWNESS;
                    } else if (player.getHealth() >= 8) {
                        thrownPotion.potionId = Potion.POISON;
                    } else if (this.distance(player) <= 3 && !player.hasEffect(Effect.WEAKNESS) && EntityUtils.rand(0, 4) == 0) {
                        thrownPotion.potionId = Potion.WEAKNESS;
                    } else {
                        thrownPotion.potionId = Potion.HARMING;
                    }

                    thrownPotion.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                            Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));
                    ProjectileLaunchEvent launch = new ProjectileLaunchEvent(thrownPotion);
                    this.server.getPluginManager().callEvent(launch);
                    if (launch.isCancelled()) {
                        thrownPotion.kill();
                    } else {
                        thrownPotion.spawnToAll();
                        this.level.addSound(this, Sound.MOB_WITCH_THROW);
                    }
                }
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
            if (EntityUtils.rand(1, 4) == 1) {
                drops.add(Item.get(Item.STICK, 0, EntityUtils.rand(0, 2)));
            }

            if (EntityUtils.rand(1, 3) == 1) {
                switch (EntityUtils.rand(1, 6)) {
                    case 1:
                        drops.add(Item.get(Item.BOTTLE, 0, EntityUtils.rand(0, 2)));
                        break;
                    case 2:
                        drops.add(Item.get(Item.GLOWSTONE_DUST, 0, EntityUtils.rand(0, 2)));
                        break;
                    case 3:
                        drops.add(Item.get(Item.GUNPOWDER, 0, EntityUtils.rand(0, 2)));
                        break;
                    case 4:
                        drops.add(Item.get(Item.REDSTONE, 0, EntityUtils.rand(0, 2)));
                        break;
                    case 5:
                        drops.add(Item.get(Item.SPIDER_EYE, 0, EntityUtils.rand(0, 2)));
                        break;
                    case 6:
                        drops.add(Item.get(Item.SUGAR, 0, EntityUtils.rand(0, 2)));
                        break;
                }
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }
}

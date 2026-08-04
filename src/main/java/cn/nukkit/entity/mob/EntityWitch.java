package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;
import cn.nukkit.utils.Utils;

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
        return 1.9f;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(26);
        super.initEntity();
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && distance <= 256; // 16 blocks
        }
        return creature.isAlive() && !creature.closed && distance <= 256;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 60 && Utils.rand(1, 3) == 2 && this.distanceSquared(player) <= 60) {
            this.attackDelay = 0;
            if (player.isAlive() && !player.closed) {
                EntityPotion shot = (EntityPotion) Entity.createEntity("ThrownPotion", this.add(0, this.getEyeHeight(), 0), this);

                if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                    shot.close();
                    return;
                }

                shot.setMotion(player.subtract(this).normalize().multiply(1.1));

                double distance = this.distanceSquared(player);

                if (!player.hasEffect(Effect.SLOWNESS) && distance <= 64) {
                    shot.potionId = Potion.SLOWNESS;
                } else if (player.getHealth() >= 8) {
                    shot.potionId = Potion.POISON;
                } else if (!player.hasEffect(Effect.WEAKNESS) && Utils.rand(0, 4) == 0 && distance <= 9) {
                    shot.potionId = Potion.WEAKNESS;
                } else {
                    shot.potionId = Potion.HARMING;
                }

                ProjectileLaunchEvent launch = new ProjectileLaunchEvent(shot);
                this.server.getPluginManager().callEvent(launch);
                if (launch.isCancelled()) {
                    shot.close();
                } else {
                    shot.spawnToAll();
                    this.level.addSound(this, Sound.MOB_WITCH_THROW);
                }
            }
        }
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        drops.add(Item.get(Item.REDSTONE, 0, Utils.rand(4, 8)));

        for (int i = 0; i < Utils.rand(1, 3); i++) {
            switch (Utils.rand(1, 7)) {
                case 1:
                    drops.add(Item.get(Item.GLOWSTONE_DUST, 0, Utils.rand(0, 2)));
                    break;
                case 2:
                    drops.add(Item.get(Item.SUGAR, 0, Utils.rand(0, 2)));
                    break;
                case 3:
                    drops.add(Item.get(Item.SPIDER_EYE, 0, Utils.rand(0, 2)));
                    break;
                case 4:
                    drops.add(Item.get(Item.GLASS_BOTTLE, 0, Utils.rand(0, 2)));
                    break;
                case 5:
                    drops.add(Item.get(Item.GUNPOWDER, 0, Utils.rand(0, 2)));
                    break;
                case 6:
                case 7:
                    drops.add(Item.get(Item.STICK, 0, Utils.rand(0, 2)));
                    break;
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    protected int nearbyDistanceMultiplier() {
        return target instanceof EntityLiving || followTarget instanceof EntityLiving ? 6 : 1;
    }

    @Override
    public boolean canDespawn() {
        return false; // TODO: swamp hut only
    }
}

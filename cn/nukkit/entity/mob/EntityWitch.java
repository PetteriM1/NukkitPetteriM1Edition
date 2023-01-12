/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityWitch
extends EntityWalkingMob {
    public static final int NETWORK_ID = 45;

    public EntityWitch(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 45;
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
    protected void initEntity() {
        this.setMaxHealth(26);
        super.initEntity();
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && d2 <= 100.0;
        }
        return entityCreature.isAlive() && !entityCreature.closed && d2 <= 100.0;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 60 && Utils.rand(1, 3) == 2 && this.distanceSquared(entity) <= 60.0) {
            this.attackDelay = 0;
            if (entity.isAlive() && !entity.closed) {
                double d2 = 1.0;
                double d3 = this.yaw + Utils.rand(-4.0, 4.0);
                Location location = new Location(this.x - Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(this.pitch)) * 0.5, this.y + (double)this.getEyeHeight(), this.z + Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(this.pitch)) * 0.5, d3, this.pitch, this.level);
                if (this.getLevel().getBlockIdAt(this.chunk, location.getFloorX(), location.getFloorY(), location.getFloorZ()) != 0) {
                    return;
                }
                EntityPotion entityPotion = (EntityPotion)Entity.createEntity("ThrownPotion", (Position)location, this);
                double d4 = this.distanceSquared(entity);
                entityPotion.potionId = !entity.hasEffect(2) && d4 <= 64.0 ? 17 : (entity.getHealth() >= 8.0f ? 25 : (!entity.hasEffect(18) && Utils.rand(0, 4) == 0 && d4 <= 9.0 ? 34 : 23));
                entityPotion.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(this.pitch)) * d2 * d2, -Math.sin(FastMathLite.toRadians(this.pitch)) * d2 * d2, Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(this.pitch)) * d2 * d2));
                ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityPotion);
                this.server.getPluginManager().callEvent(projectileLaunchEvent);
                if (projectileLaunchEvent.isCancelled()) {
                    entityPotion.close();
                } else {
                    entityPotion.spawnToAll();
                    this.level.addSound((Vector3)this, Sound.MOB_WITCH_THROW);
                }
            }
        }
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (Utils.rand(1, 4) == 1) {
            arrayList.add(Item.get(280, 0, Utils.rand(0, 2)));
        }
        if (Utils.rand(1, 3) == 1) {
            switch (Utils.rand(1, 6)) {
                case 1: {
                    arrayList.add(Item.get(374, 0, Utils.rand(0, 2)));
                    break;
                }
                case 2: {
                    arrayList.add(Item.get(348, 0, Utils.rand(0, 2)));
                    break;
                }
                case 3: {
                    arrayList.add(Item.get(289, 0, Utils.rand(0, 2)));
                    break;
                }
                case 4: {
                    arrayList.add(Item.get(331, 0, Utils.rand(0, 2)));
                    break;
                }
                case 5: {
                    arrayList.add(Item.get(375, 0, Utils.rand(0, 2)));
                    break;
                }
                case 6: {
                    arrayList.add(Item.get(353, 0, Utils.rand(0, 2)));
                }
            }
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 8;
    }
}


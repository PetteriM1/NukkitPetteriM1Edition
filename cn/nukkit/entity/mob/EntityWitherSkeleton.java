/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector2;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityWitherSkeleton
extends EntityWalkingMob
implements EntitySmite {
    public static final int NETWORK_ID = 48;

    public EntityWitherSkeleton(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 48;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        this.fireProof = true;
        this.setDamage(new int[]{0, 5, 8, 12});
    }

    @Override
    public float getWidth() {
        return 0.7f;
    }

    @Override
    public float getHeight() {
        return 2.4f;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && entity.distanceSquared(this) <= 1.0) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> hashMap = new HashMap<EntityDamageEvent.DamageModifier, Float>();
            hashMap.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(this.getDamage()));
            if (entity instanceof Player) {
                float f2 = 0.0f;
                for (Item item : ((Player)entity).getInventory().getArmorContents()) {
                    f2 += this.getArmorPoints(item.getId());
                }
                hashMap.put(EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf((float)((double)hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf(0.0f)).floatValue() - Math.floor((double)(hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.BASE, Float.valueOf(1.0f)).floatValue() * f2) * 0.04))));
            }
            if (entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap))) {
                this.playAttack();
                entity.addEffect(Effect.getEffect(20).setDuration(200));
            }
        }
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.eid = this.getId();
        mobEquipmentPacket.item = Item.get(272);
        mobEquipmentPacket.hotbarSlot = 0;
        player.dataPacket(mobEquipmentPacket);
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (int k = 0; k < Utils.rand(0, 2); ++k) {
            arrayList.add(Item.get(352, 0, 1));
        }
        if (Utils.rand(1, 3) == 1) {
            arrayList.add(Item.get(263, 0, 1));
        }
        if (Utils.rand(1, 40) == 1) {
            arrayList.add(Item.get(397, 1, 1));
        }
        if (Utils.rand(1, 200) <= 17) {
            arrayList.add(Item.get(272, Utils.rand(0, 131), 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Wither Skeleton";
    }

    @Override
    public void kill() {
        Entity entity;
        if (!this.isAlive()) {
            return;
        }
        super.kill();
        if (this.lastDamageCause instanceof EntityDamageByChildEntityEvent && ((EntityDamageByChildEntityEvent)this.lastDamageCause).getChild() instanceof EntityArrow && (entity = ((EntityDamageByChildEntityEvent)this.lastDamageCause).getDamager()) instanceof Player) {
            Vector2 vector2 = new Vector2(this.x, this.z);
            Vector2 vector22 = new Vector2(entity.x, entity.z);
            if (vector2.distance(vector22) >= 50.0) {
                ((Player)entity).awardAchievement("snipeSkeleton");
            }
        }
    }
}


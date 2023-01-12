/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;
import java.util.HashMap;

public class EntityVindicator
extends EntityWalkingMob {
    public static final int NETWORK_ID = 57;
    private boolean w;

    public EntityVindicator(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 57;
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
        return 1.2;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(24);
        super.initEntity();
        this.setDamage(new int[]{0, 2, 3, 4});
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
            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(388, 0, Utils.rand(0, 1))};
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        if (!this.closed) {
            if (this.followTarget != null) {
                if (!this.w) {
                    this.w = true;
                    this.setDataFlag(0, 25, true);
                }
                if (this.getDataPropertyLong(6) != this.followTarget.getId()) {
                    this.setDataProperty(new LongEntityData(6, this.followTarget.getId()));
                }
            } else {
                if (this.w) {
                    this.w = false;
                    this.setDataFlag(0, 25, false);
                }
                if (this.getDataPropertyLong(6) != 0L) {
                    this.setDataProperty(new LongEntityData(6, 0L));
                }
            }
        }
        return super.entityBaseTick(n);
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.eid = this.getId();
        mobEquipmentPacket.item = Item.get(258);
        mobEquipmentPacket.hotbarSlot = 0;
        player.dataPacket(mobEquipmentPacket);
    }
}


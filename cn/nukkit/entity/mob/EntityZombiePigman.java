/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityZombiePigman
extends EntityWalkingMob
implements EntitySmite {
    public static final int NETWORK_ID = 36;
    private int w = 0;

    public EntityZombiePigman(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 36;
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
        return 1.15;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        if (this.namedTag.contains("Angry")) {
            this.w = this.namedTag.getInt("Angry");
        }
        this.fireProof = true;
        this.setDamage(new int[]{0, 5, 9, 13});
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Angry", this.w);
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (d2 <= 100.0 && this.isAngry() && entityCreature instanceof EntityZombiePigman && !((EntityZombiePigman)entityCreature).isAngry()) {
            ((EntityZombiePigman)entityCreature).setAngry(2400);
        }
        return this.isAngry() && super.targetOption(entityCreature, d2);
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && this.distanceSquared(entity) < 1.44) {
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
            this.playAttack();
        }
    }

    public boolean isAngry() {
        return this.w > 0;
    }

    public void setAngry(int n) {
        this.w = n;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled() && entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
            this.setAngry(2400);
        }
        return true;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.eid = this.getId();
        mobEquipmentPacket.item = Item.get(283);
        mobEquipmentPacket.inventorySlot = 0;
        player.dataPacket(mobEquipmentPacket);
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            arrayList.add(Item.get(367, 0, Utils.rand(0, 1)));
            arrayList.add(Item.get(371, 0, Utils.rand(0, 1)));
            for (int k = 0; k < (Utils.rand(0, 101) <= 9 ? 1 : 0); ++k) {
                arrayList.add(Item.get(283, Utils.rand(20, 30), 1));
            }
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Zombie Pigman";
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        if (this.w > 0) {
            --this.w;
        }
        return super.entityBaseTick(n);
    }
}


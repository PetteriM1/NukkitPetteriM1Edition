/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntitySwimmingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityElderGuardian
extends EntitySwimmingMob {
    public static final int NETWORK_ID = 50;

    public EntityElderGuardian(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 50;
    }

    @Override
    public float getWidth() {
        return 1.9975f;
    }

    @Override
    public float getHeight() {
        return 1.9975f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(80);
        super.initEntity();
        this.setDataFlag(0, 33, true);
        this.setDamage(new int[]{0, 5, 8, 12});
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return false;
    }

    @Override
    public void attackEntity(Entity entity) {
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (int k = 0; k < Utils.rand(0, 2); ++k) {
            arrayList.add(Item.get(409, 0, 1));
        }
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)this.lastDamageCause).getDamager() instanceof Player) {
            arrayList.add(Item.get(19, 1, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 10;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Elder Guardian";
    }
}


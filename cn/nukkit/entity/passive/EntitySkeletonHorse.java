/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.passive.EntityHorseBase;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntitySkeletonHorse
extends EntityHorseBase
implements EntitySmite {
    public static final int NETWORK_ID = 26;

    public EntitySkeletonHorse(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 26;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.6982f;
        }
        return 1.3965f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.8f;
        }
        return 1.6f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(15);
        super.initEntity();
    }

    @Override
    public boolean isFeedItem(Item item) {
        return false;
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return false;
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            for (int k = 0; k < Utils.rand(0, 2); ++k) {
                arrayList.add(Item.get(334, 0, 1));
            }
            arrayList.add(Item.get(352, 0, Utils.rand(0, 1)));
        }
        if (this.isSaddled()) {
            arrayList.add(Item.get(329, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Skeleton Horse";
    }
}


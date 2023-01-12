/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.passive.EntityHorseBase;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityZombieHorse
extends EntityHorseBase
implements EntitySmite {
    public static final int NETWORK_ID = 27;

    public EntityZombieHorse(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 27;
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
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            int n;
            for (n = 0; n < Utils.rand(0, 2); ++n) {
                arrayList.add(Item.get(334, 0, 1));
            }
            for (n = 0; n < Utils.rand(0, 2); ++n) {
                arrayList.add(Item.get(367, 0, 1));
            }
        }
        if (this.isSaddled()) {
            arrayList.add(Item.get(329, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public String getName() {
        return "Zombie Horse";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityCat
extends EntityWalkingAnimal {
    public static final int NETWORK_ID = 75;

    public EntityCat(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 75;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.3f;
        }
        return 0.6f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.35f;
        }
        return 0.7f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(10);
        super.initEntity();
        this.noFallDamage = true;
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            for (int k = 0; k < Utils.rand(0, 2); ++k) {
                arrayList.add(Item.get(287, 0, 1));
            }
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            int n = player.getInventory().getItemInHandFast().getId();
            return player.spawned && player.isAlive() && !player.closed && (n == 349 || n == 460) && d2 <= 40.0;
        }
        return super.targetOption(entityCreature, d2);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.EntityHorseBase;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityDonkey
extends EntityHorseBase {
    public static final int NETWORK_ID = 24;
    private boolean w;

    public EntityDonkey(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 24;
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
        if (this.namedTag.contains("ChestedHorse")) {
            this.setChested(this.namedTag.getBoolean("ChestedHorse"));
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        boolean bl = super.targetOption(entityCreature, d2);
        if (bl && entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return player.spawned && player.isAlive() && !player.closed && this.isFeedItem(player.getInventory().getItemInHandFast()) && d2 <= 40.0;
        }
        return false;
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            for (int k = 0; k < Utils.rand(0, 2); ++k) {
                arrayList.add(Item.get(334, 0, 1));
            }
        }
        if (this.isChested()) {
            arrayList.add(Item.get(54, 0, 1));
        }
        if (this.isSaddled()) {
            arrayList.add(Item.get(329, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (!this.isBaby() && !this.isChested() && item.getId() == 54) {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            this.setChested(true);
            return false;
        }
        return super.onInteract(player, item, vector3);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("ChestedHorse", this.isChested());
    }

    public boolean isChested() {
        return this.w;
    }

    public void setChested(boolean bl) {
        this.w = bl;
        this.setDataFlag(0, 36, bl);
    }
}


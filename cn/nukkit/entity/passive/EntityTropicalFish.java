/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.passive.EntityFish;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityTropicalFish
extends EntityFish {
    public static final int NETWORK_ID = 111;
    private int t;
    private int u;
    private int v;

    public EntityTropicalFish(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(3);
        super.initEntity();
        this.t = this.namedTag.contains("VariantA") ? this.namedTag.getInt("VariantA") : Utils.rand(0, 5);
        this.dataProperties.putInt(2, this.t);
        this.u = this.namedTag.contains("VariantB") ? this.namedTag.getInt("VariantB") : Utils.rand(0, 5);
        this.dataProperties.putInt(43, this.u);
        this.v = this.namedTag.contains("Color") ? this.namedTag.getInt("Color") : Utils.rand(0, 15);
        this.dataProperties.putByte(3, this.v);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("VariantA", this.t);
        this.namedTag.putInt("VariantB", this.u);
        this.namedTag.putInt("Color", this.v);
    }

    int c() {
        return 4;
    }

    @Override
    public int getNetworkId() {
        return 111;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.4f;
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(461, 0, 1), Item.get(352, 0, Utils.rand(0, 2))};
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Tropical Fish";
    }
}


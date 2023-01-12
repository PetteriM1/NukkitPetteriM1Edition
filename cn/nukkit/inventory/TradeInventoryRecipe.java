/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;

public class TradeInventoryRecipe {
    public static int A_ITEM = 0;
    public static int B_ITEM = 1;
    private final Item e;
    private final Item h;
    private final Item j;
    private int a = -1;
    private int c = 999;
    private int g = 0;
    private int f = 0;
    private final int b = 0;
    private int i = 0;
    private int m = 0;
    private final int d = 0;
    private float k = 0.0f;
    private float l = 0.0f;

    public TradeInventoryRecipe(Item item, Item item2) {
        this(item, item2, Item.get(0));
    }

    public TradeInventoryRecipe(Item item, Item item2, Item item3) {
        this.e = item;
        this.h = item2;
        this.j = item3;
    }

    public TradeInventoryRecipe setTier(int n) {
        this.a = n;
        return this;
    }

    public TradeInventoryRecipe setMaxUses(int n) {
        this.c = n;
        return this;
    }

    public TradeInventoryRecipe setBuyCount(int n, int n2) {
        switch (n2) {
            case 0: {
                this.g = n;
                break;
            }
            case 1: {
                this.f = n;
            }
        }
        this.g = n;
        return this;
    }

    public TradeInventoryRecipe setDemand(int n) {
        this.i = n;
        return this;
    }

    public TradeInventoryRecipe setMultiplier(float f2, int n) {
        switch (n) {
            case 0: {
                this.k = f2;
                break;
            }
            case 1: {
                this.l = f2;
            }
        }
        return this;
    }

    public TradeInventoryRecipe setRewardExp(int n) {
        this.m = n;
        return this;
    }

    public CompoundTag toNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putCompound("buyA", NBTIO.putItemHelper(this.h, -1));
        compoundTag.putCompound("buyB", NBTIO.putItemHelper(this.j, -1));
        compoundTag.putCompound("sell", NBTIO.putItemHelper(this.e, -1));
        compoundTag.putInt("tier", this.a);
        compoundTag.putInt("buyCountA", this.g);
        compoundTag.putInt("buyCountB", this.f);
        compoundTag.putInt("uses", 0);
        compoundTag.putInt("maxUses", this.c);
        compoundTag.putInt("rewardExp", this.m);
        compoundTag.putInt("demand", this.i);
        compoundTag.putInt("traderExp", 0);
        compoundTag.putFloat("priceMultiplierA", this.k);
        compoundTag.putFloat("priceMultiplierB", this.l);
        return compoundTag;
    }

    public static TradeInventoryRecipe toNBT(CompoundTag compoundTag) {
        return new TradeInventoryRecipe(NBTIO.getItemHelper(compoundTag.getCompound("sell")), NBTIO.getItemHelper(compoundTag.getCompound("buyA")), NBTIO.getItemHelper(compoundTag.getCompound("buyB"))).setTier(compoundTag.getInt("tier")).setBuyCount(compoundTag.getInt("buyCountA"), A_ITEM).setBuyCount(compoundTag.getInt("buyCountB"), B_ITEM).setMaxUses(compoundTag.getInt("maxUses")).setMultiplier(compoundTag.getInt("priceMultiplierA"), A_ITEM).setMultiplier(compoundTag.getInt("priceMultiplierB"), B_ITEM).setDemand(compoundTag.getInt("demand")).setRewardExp(compoundTag.getInt("rewardExp"));
    }

    public Item getSellItem() {
        return this.e;
    }

    public Item getBuyItem() {
        return this.h;
    }

    public Item getSecondBuyItem() {
        return this.j;
    }
}


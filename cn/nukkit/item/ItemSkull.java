/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemSkull
extends Item {
    public static final int SKELETON_SKULL = 0;
    public static final int WITHER_SKELETON_SKULL = 1;
    public static final int ZOMBIE_HEAD = 2;
    public static final int HEAD = 3;
    public static final int CREEPER_HEAD = 4;
    public static final int DRAGON_HEAD = 5;

    public ItemSkull() {
        this((Integer)0, 1);
    }

    public ItemSkull(Integer n) {
        this(n, 1);
    }

    public ItemSkull(Integer n, int n2) {
        super(397, n, n2, ItemSkull.getItemSkullName(n));
        this.block = Block.get(144);
    }

    public static String getItemSkullName(int n) {
        switch (n) {
            case 1: {
                return "Wither Skeleton Skull";
            }
            case 2: {
                return "Zombie Head";
            }
            case 3: {
                return "Head";
            }
            case 4: {
                return "Creeper Head";
            }
            case 5: {
                return "Dragon Head";
            }
        }
        return "Skeleton Skull";
    }

    @Override
    public boolean isHelmet() {
        return true;
    }

    @Override
    public boolean canBePutInHelmetSlot() {
        return true;
    }

    public static Item getMobHead(int n) {
        switch (n) {
            case 34: {
                return Item.get(397, 0, 1);
            }
            case 48: {
                return Item.get(397, 1, 1);
            }
            case 32: {
                return Item.get(397, 2, 1);
            }
            case 33: {
                return Item.get(397, 4, 1);
            }
        }
        return null;
    }
}


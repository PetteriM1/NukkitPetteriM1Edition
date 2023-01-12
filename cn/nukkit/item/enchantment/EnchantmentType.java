/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemBow;
import cn.nukkit.item.ItemCrossbow;
import cn.nukkit.item.ItemFishingRod;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.item.ItemTrident;
import cn.nukkit.item.enchantment.b;

public enum EnchantmentType {
    ALL,
    ARMOR,
    ARMOR_HEAD,
    ARMOR_TORSO,
    ARMOR_LEGS,
    ARMOR_FEET,
    SWORD,
    DIGGER,
    FISHING_ROD,
    BREAKABLE,
    BOW,
    WEARABLE,
    TRIDENT,
    CROSSBOW;


    public boolean canEnchantItem(Item item) {
        if (this == ALL) {
            return true;
        }
        if (this == BREAKABLE && item.getMaxDurability() >= 0) {
            return true;
        }
        if (item instanceof ItemArmor) {
            if (this == WEARABLE || this == ARMOR && item.isArmor()) {
                return true;
            }
            switch (b.a[this.ordinal()]) {
                case 1: {
                    return item.isHelmet();
                }
                case 2: {
                    return item.isChestplate();
                }
                case 3: {
                    return item.isLeggings();
                }
                case 4: {
                    return item.isBoots();
                }
            }
            return false;
        }
        switch (b.a[this.ordinal()]) {
            case 5: {
                return item.isSword() && item.getId() != 455;
            }
            case 6: {
                return item.isPickaxe() || item.isShovel() || item.isAxe() || item.isHoe();
            }
            case 7: {
                return item instanceof ItemBow;
            }
            case 8: {
                return item instanceof ItemFishingRod;
            }
            case 9: {
                return item instanceof ItemSkull || item.getId() == -155;
            }
            case 10: {
                return item instanceof ItemTrident;
            }
            case 11: {
                return item instanceof ItemCrossbow;
            }
        }
        return false;
    }
}


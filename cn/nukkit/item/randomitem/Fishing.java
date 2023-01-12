/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.randomitem;

import cn.nukkit.item.Item;
import cn.nukkit.item.randomitem.ConstantItemSelector;
import cn.nukkit.item.randomitem.RandomItem;
import cn.nukkit.item.randomitem.Selector;
import cn.nukkit.utils.DyeColor;

public final class Fishing {
    public static final Selector ROOT_FISHING = RandomItem.putSelector(new Selector(RandomItem.ROOT));
    public static final Selector FISHES = RandomItem.putSelector(new Selector(ROOT_FISHING), 0.85f);
    public static final Selector TREASURES = RandomItem.putSelector(new Selector(ROOT_FISHING), 0.05f);
    public static final Selector JUNKS = RandomItem.putSelector(new Selector(ROOT_FISHING), 0.1f);
    public static final Selector FISH = RandomItem.putSelector(new ConstantItemSelector(349, FISHES), 0.6f);
    public static final Selector SALMON = RandomItem.putSelector(new ConstantItemSelector(460, FISHES), 0.25f);
    public static final Selector CLOWNFISH = RandomItem.putSelector(new ConstantItemSelector(461, FISHES), 0.02f);
    public static final Selector PUFFERFISH = RandomItem.putSelector(new ConstantItemSelector(462, FISHES), 0.13f);
    public static final Selector TREASURE_BOW = RandomItem.putSelector(new ConstantItemSelector(261, TREASURES), 0.1667f);
    public static final Selector TREASURE_ENCHANTED_BOOK = RandomItem.putSelector(new ConstantItemSelector(403, TREASURES), 0.1667f);
    public static final Selector TREASURE_FISHING_ROD = RandomItem.putSelector(new ConstantItemSelector(346, TREASURES), 0.1667f);
    public static final Selector TREASURE_NAME_TAG = RandomItem.putSelector(new ConstantItemSelector(421, TREASURES), 0.1667f);
    public static final Selector TREASURE_SADDLE = RandomItem.putSelector(new ConstantItemSelector(329, TREASURES), 0.1667f);
    public static final Selector TREASURE_NAUTILUS_SHELL = RandomItem.putSelector(new ConstantItemSelector(465, TREASURES), 0.1667f);
    public static final Selector JUNK_BOWL = RandomItem.putSelector(new ConstantItemSelector(281, JUNKS), 0.12f);
    public static final Selector JUNK_FISHING_ROD = RandomItem.putSelector(new ConstantItemSelector(346, JUNKS), 0.024f);
    public static final Selector JUNK_LEATHER = RandomItem.putSelector(new ConstantItemSelector(334, JUNKS), 0.12f);
    public static final Selector JUNK_LEATHER_BOOTS = RandomItem.putSelector(new ConstantItemSelector(301, JUNKS), 0.12f);
    public static final Selector JUNK_ROTTEN_FLESH = RandomItem.putSelector(new ConstantItemSelector(367, JUNKS), 0.12f);
    public static final Selector JUNK_STICK = RandomItem.putSelector(new ConstantItemSelector(280, JUNKS), 0.06f);
    public static final Selector JUNK_STRING_ITEM = RandomItem.putSelector(new ConstantItemSelector(287, JUNKS), 0.06f);
    public static final Selector JUNK_WATTER_BOTTLE = RandomItem.putSelector(new ConstantItemSelector(373, 0, JUNKS), 0.12f);
    public static final Selector JUNK_BONE = RandomItem.putSelector(new ConstantItemSelector(352, JUNKS), 0.12f);
    public static final Selector JUNK_INK_SAC = RandomItem.putSelector(new ConstantItemSelector(351, DyeColor.BLACK.getDyeData(), 10, JUNKS), 0.012f);
    public static final Selector JUNK_TRIPWIRE_HOOK = RandomItem.putSelector(new ConstantItemSelector(131, JUNKS), 0.12f);

    public static Item getFishingResult(Item item) {
        int n = 0;
        int n2 = 0;
        if (item != null) {
            if (item.hasEnchantment(23)) {
                n = item.getEnchantment(23).getLevel();
            } else if (item.hasEnchantment(24)) {
                n2 = item.getEnchantment(24).getLevel();
            }
        }
        return Fishing.getFishingResult(n, n2);
    }

    public static Item getFishingResult(int n, int n2) {
        float f2 = Fishing.a(0.05f + 0.01f * (float)n - 0.01f * (float)n2);
        float f3 = Fishing.a(0.05f - 0.025f * (float)n - 0.01f * (float)n2);
        float f4 = Fishing.a(1.0f - f2 - f3);
        RandomItem.putSelector(FISHES, f4);
        RandomItem.putSelector(TREASURES, f2);
        RandomItem.putSelector(JUNKS, f3);
        Object object = RandomItem.a(ROOT_FISHING);
        if (object instanceof Item) {
            return (Item)object;
        }
        return null;
    }

    private static float a(float f2) {
        if (f2 >= 1.0f) {
            return 1.0f;
        }
        return Math.max(f2, 0.0f);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDurable;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Utils;

public abstract class ItemTool
extends Item
implements ItemDurable {
    public static final int TIER_WOODEN = 1;
    public static final int TIER_GOLD = 2;
    public static final int TIER_STONE = 3;
    public static final int TIER_IRON = 4;
    public static final int TIER_DIAMOND = 5;
    public static final int TIER_NETHERITE = 6;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_SWORD = 1;
    public static final int TYPE_SHOVEL = 2;
    public static final int TYPE_PICKAXE = 3;
    public static final int TYPE_AXE = 4;
    public static final int TYPE_SHEARS = 5;
    public static final int TYPE_HOE = 6;
    public static final int DURABILITY_WOODEN = 60;
    public static final int DURABILITY_GOLD = 33;
    public static final int DURABILITY_STONE = 132;
    public static final int DURABILITY_IRON = 251;
    public static final int DURABILITY_DIAMOND = 1562;
    public static final int DURABILITY_NETHERITE = 2032;
    public static final int DURABILITY_FLINT_STEEL = 65;
    public static final int DURABILITY_SHEARS = 239;
    public static final int DURABILITY_BOW = 385;
    public static final int DURABILITY_CROSSBOW = 385;
    public static final int DURABILITY_TRIDENT = 251;
    public static final int DURABILITY_FISHING_ROD = 65;
    public static final int DURABILITY_CARROT_ON_A_STICK = 25;
    public static final int DURABILITY_WARPED_FUNGUS_ON_A_STICK = 100;

    public ItemTool(int n) {
        this(n, 0, 1, "Unknown");
    }

    public ItemTool(int n, Integer n2) {
        this(n, n2, 1, "Unknown");
    }

    public ItemTool(int n, Integer n2, int n3) {
        this(n, n2, n3, "Unknown");
    }

    public ItemTool(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean useOn(Block block) {
        if (this.isUnbreakable() || this.a() || this.noDamageOnBreak()) {
            return true;
        }
        if (block.getToolType() == 3 && this.isPickaxe() || block.getToolType() == 2 && this.isShovel() || block.getToolType() == 4 && this.isAxe() || block.getToolType() == 6 && this.isHoe() || block.getToolType() == 1 && this.isSword() || block.getToolType() == 5 && this.isShears()) {
            ++this.meta;
        } else if (this.isSword() && block.getHardness() > 0.0) {
            this.meta += 2;
        } else if (this.isHoe()) {
            if (block.getId() == 2 || block.getId() == 3) {
                ++this.meta;
            }
        } else {
            ++this.meta;
        }
        return true;
    }

    @Override
    public boolean useOn(Entity entity) {
        if (this.isUnbreakable() || this.a() || this.noDamageOnAttack()) {
            return true;
        }
        this.meta = entity != null && !this.isSword() ? (this.meta += 2) : ++this.meta;
        return true;
    }

    private boolean a() {
        if (!this.hasEnchantments()) {
            return false;
        }
        Enchantment enchantment = this.getEnchantment(17);
        return enchantment != null && enchantment.getLevel() > 0 && 100 / (enchantment.getLevel() + 1) <= Utils.random.nextInt(100);
    }

    @Override
    public boolean isUnbreakable() {
        Tag tag = this.getNamedTagEntry("Unbreakable");
        return tag instanceof ByteTag && ((ByteTag)tag).data > 0;
    }

    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public int getEnchantAbility() {
        switch (this.getTier()) {
            case 3: {
                return 5;
            }
            case 1: {
                return 15;
            }
            case 5: {
                return 10;
            }
            case 2: {
                return 22;
            }
            case 4: {
                return 14;
            }
            case 6: {
                return 10;
            }
        }
        return 0;
    }

    public boolean noDamageOnAttack() {
        return false;
    }

    public boolean noDamageOnBreak() {
        return false;
    }
}


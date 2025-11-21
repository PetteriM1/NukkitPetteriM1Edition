package cn.nukkit.item.enchantment;

/**
 * @author Nukkit Project Team
 */
public class EnchantmentList {

    private final EnchantmentEntry[] enchantments;

    public EnchantmentList(int size) {
        this.enchantments = new EnchantmentEntry[size];
    }

    public int getSize() {
        return enchantments.length;
    }

    public EnchantmentEntry getSlot(int slot) {
        return enchantments[slot];
    }

    public EnchantmentList setSlot(int slot, EnchantmentEntry entry) {
        enchantments[slot] = entry;
        return this;
    }
}

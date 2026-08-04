package cn.nukkit.item;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class ItemBookEnchanted extends Item {

    public ItemBookEnchanted() {
        this(0, 1);
    }

    public ItemBookEnchanted(Integer meta) {
        this(meta, 1);
    }

    public ItemBookEnchanted(Integer meta, int count) {
        super(ENCHANTED_BOOK, meta, count, "Enchanted Book");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        if (protocol >= ProtocolInfo.v1_21_130_28) {
            return true;
        }

        for (Enchantment e : this.getEnchantments()) {
            int id = e.getId();
            if (id == Enchantment.ID_LUNGE && protocol < ProtocolInfo.v1_21_130_28) {
                return false;
            }
            if (protocol < ProtocolInfo.v1_21_0 &&
                    (id == Enchantment.ID_BREACH || id == Enchantment.ID_DENSITY || id == Enchantment.ID_WIND_BURST)) {
                return false;
            }
            if (id == Enchantment.ID_SWIFT_SNEAK && protocol < ProtocolInfo.v1_19_0) {
                return false;
            }
            if (id == Enchantment.ID_SOUL_SPEED && protocol < ProtocolInfo.v1_16_0) {
                return false;
            }
        }

        return true;
    }
}

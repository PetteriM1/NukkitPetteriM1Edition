package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemBootsNetherite extends ItemArmor {

    public ItemBootsNetherite() {
        this(0, 1);
    }

    public ItemBootsNetherite(Integer meta) {
        this(meta, 1);
    }

    public ItemBootsNetherite(Integer meta, int count) {
        super(NETHERITE_BOOTS, meta, count, "Netherite Boots");
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_NETHERITE;
    }

    @Override
    public int getMaxDurability() {
        return 481;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getToughness() {
        return 3;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_16_0;
    }
}

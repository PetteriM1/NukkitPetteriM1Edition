package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemLeggingsNetherite extends ItemArmor {

    public ItemLeggingsNetherite() {
        this(0, 1);
    }

    public ItemLeggingsNetherite(Integer meta) {
        this(meta, 1);
    }

    public ItemLeggingsNetherite(Integer meta, int count) {
        super(NETHERITE_LEGGINGS, meta, count, "Netherite Leggings");
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_NETHERITE;
    }

    @Override
    public int getMaxDurability() {
        return 555;
    }

    @Override
    public int getArmorPoints() {
        return 6;
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

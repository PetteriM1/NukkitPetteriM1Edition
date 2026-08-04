package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemWayfinderArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemWayfinderArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemWayfinderArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemWayfinderArmorTrimSmithingTemplate(Integer meta, int count) {
        super(WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Wayfinder Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.WAYFINDER;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

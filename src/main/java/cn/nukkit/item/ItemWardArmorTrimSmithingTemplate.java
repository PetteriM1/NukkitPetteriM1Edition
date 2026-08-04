package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemWardArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemWardArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemWardArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemWardArmorTrimSmithingTemplate(Integer meta, int count) {
        super(WARD_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Ward Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.WARD;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

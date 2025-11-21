package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemShaperArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemShaperArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemShaperArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemShaperArmorTrimSmithingTemplate(Integer meta, int count) {
        super(SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Shaper Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.SHAPER;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

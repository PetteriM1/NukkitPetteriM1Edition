package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemCoastArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemCoastArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemCoastArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemCoastArmorTrimSmithingTemplate(Integer meta, int count) {
        super(COAST_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Coast Armor Trim");
    }

    @Override
    public ItemTrimPattern.Type getPattern() {
        return Type.COAST;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

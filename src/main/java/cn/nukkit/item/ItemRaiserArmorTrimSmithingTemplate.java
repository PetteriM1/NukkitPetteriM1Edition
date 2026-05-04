package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRaiserArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemRaiserArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemRaiserArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemRaiserArmorTrimSmithingTemplate(Integer meta, int count) {
        super(RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Raiser Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.RAISER;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

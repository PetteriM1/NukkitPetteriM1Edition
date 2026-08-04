package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemEyeArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemEyeArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemEyeArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemEyeArmorTrimSmithingTemplate(Integer meta, int count) {
        super(EYE_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Eye Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.EYE;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

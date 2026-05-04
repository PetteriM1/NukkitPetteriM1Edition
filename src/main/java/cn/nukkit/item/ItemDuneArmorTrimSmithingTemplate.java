package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemDuneArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemDuneArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemDuneArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemDuneArmorTrimSmithingTemplate(Integer meta, int count) {
        super(DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Dune Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.DUNE;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemBoltArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemBoltArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemBoltArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemBoltArmorTrimSmithingTemplate(Integer meta, int count) {
        super(BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Bolt Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.BOLT;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}

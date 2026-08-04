package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemSentryArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemSentryArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemSentryArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemSentryArmorTrimSmithingTemplate(Integer meta, int count) {
        super(SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Sentry Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.SENTRY;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

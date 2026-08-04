package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemFlowArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemFlowArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemFlowArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemFlowArmorTrimSmithingTemplate(Integer meta, int count) {
        super(FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Flow Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.FLOW;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}

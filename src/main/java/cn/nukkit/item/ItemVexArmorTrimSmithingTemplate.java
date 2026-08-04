package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemVexArmorTrimSmithingTemplate extends Item implements ItemTrimPattern {

    public ItemVexArmorTrimSmithingTemplate() {
        this(0, 1);
    }

    public ItemVexArmorTrimSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemVexArmorTrimSmithingTemplate(Integer meta, int count) {
        super(VEX_ARMOR_TRIM_SMITHING_TEMPLATE, meta, count, "Vex Armor Trim");
    }

    @Override
    public Type getPattern() {
        return Type.VEX;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

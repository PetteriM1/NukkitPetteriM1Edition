package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemNetheriteUpgradeSmithingTemplate extends Item {

    public ItemNetheriteUpgradeSmithingTemplate() {
        this(0, 1);
    }

    public ItemNetheriteUpgradeSmithingTemplate(Integer meta) {
        this(meta, 1);
    }

    public ItemNetheriteUpgradeSmithingTemplate(Integer meta, int count) {
        super(NETHERITE_UPGRADE_SMITHING_TEMPLATE, meta, count, "Netherite Upgrade");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

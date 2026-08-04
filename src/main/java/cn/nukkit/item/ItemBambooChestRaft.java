package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemBambooChestRaft extends ItemChestBoat {

    public ItemBambooChestRaft() {
        this(0, 1);
    }

    public ItemBambooChestRaft(Integer meta) {
        this(meta, 1);
    }

    public ItemBambooChestRaft(Integer meta, int count) {
        super(BAMBOO_CHEST_RAFT, meta, count, "Bamboo Raft with Chest");
    }

    @Override
    public int getVariant() {
        return 7;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

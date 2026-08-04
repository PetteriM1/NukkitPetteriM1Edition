package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemBreezeRod extends Item {

    public ItemBreezeRod() {
        this(0, 1);
    }

    public ItemBreezeRod(Integer meta) {
        this(meta, 1);
    }

    public ItemBreezeRod(Integer meta, int count) {
        super(BREEZE_ROD, meta, count, "Breeze Rod");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}

package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemGoldRaw extends Item {

    public ItemGoldRaw() {
        this(0, 1);
    }

    public ItemGoldRaw(Integer meta) {
        this(meta, 1);
    }

    public ItemGoldRaw(Integer meta, int count) {
        super(RAW_GOLD, meta, count, "Raw Gold");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_17_0;
    }
}

package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemCopperRaw extends Item {

    public ItemCopperRaw() {
        this(0, 1);
    }

    public ItemCopperRaw(Integer meta) {
        this(meta, 1);
    }

    public ItemCopperRaw(Integer meta, int count) {
        super(RAW_COPPER, meta, count, "Raw Copper");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_17_0;
    }
}

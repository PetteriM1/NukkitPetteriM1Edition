package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemIronRaw extends Item {

    public ItemIronRaw() {
        this(0, 1);
    }

    public ItemIronRaw(Integer meta) {
        this(meta, 1);
    }

    public ItemIronRaw(Integer meta, int count) {
        super(RAW_IRON, meta, count, "Raw Iron");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_17_0;
    }
}

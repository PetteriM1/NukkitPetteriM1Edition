package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemScute extends Item {

    public ItemScute() {
        this(0, 1);
    }

    public ItemScute(Integer meta) {
        this(meta, 1);
    }

    public ItemScute(Integer meta, int count) {
        super(SCUTE, meta, count, "Scute");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_5_0;
    }
}

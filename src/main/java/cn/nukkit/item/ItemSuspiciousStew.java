package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemSuspiciousStew extends ItemEdible {

    public ItemSuspiciousStew() {
        this(0, 1);
    }

    public ItemSuspiciousStew(Integer meta) {
        this(meta, 1);
    }

    public ItemSuspiciousStew(Integer meta, int count) {
        super(SUSPICIOUS_STEW, meta, count, "Suspicious Stew");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_13_0;
    }
}

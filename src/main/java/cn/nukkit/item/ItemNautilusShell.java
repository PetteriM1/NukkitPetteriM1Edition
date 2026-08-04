package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemNautilusShell extends Item {

    public ItemNautilusShell() {
        this(0, 1);
    }

    public ItemNautilusShell(Integer meta) {
        this(meta, 1);
    }

    public ItemNautilusShell(Integer meta, int count) {
        super(NAUTILUS_SHELL, meta, count, "Nautilus Shell");
    }

    @Override
    public boolean allowOffhand() {
        return true;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_4_0;
    }
}

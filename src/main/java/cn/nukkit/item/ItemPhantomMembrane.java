package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemPhantomMembrane extends Item {

    public ItemPhantomMembrane() {
        this(0, 1);
    }

    public ItemPhantomMembrane(Integer meta) {
        this(meta, 1);
    }

    public ItemPhantomMembrane(Integer meta, int count) {
        super(PHANTOM_MEMBRANE, meta, count, "Phantom Membrane");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_6_0;
    }
}

package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemOminousBottle extends Item {

    public ItemOminousBottle() {
        this(0, 1);
    }

    public ItemOminousBottle(Integer meta) {
        this(meta, 1);
    }

    public ItemOminousBottle(Integer meta, int count) {
        super(OMINOUS_BOTTLE, meta, count, "Ominous Bottle");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}

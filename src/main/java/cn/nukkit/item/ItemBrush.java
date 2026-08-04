package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemBrush extends Item {

    public ItemBrush() {
        this(0, 1);
    }

    public ItemBrush(Integer meta) {
        this(meta, 1);
    }

    public ItemBrush(Integer meta, int count) {
        super(BRUSH, meta, count, "Brush");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

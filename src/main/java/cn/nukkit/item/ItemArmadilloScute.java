package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemArmadilloScute extends Item {

    public ItemArmadilloScute() {
        this(0, 1);
    }

    public ItemArmadilloScute(Integer meta) {
        this(meta, 1);
    }

    public ItemArmadilloScute(Integer meta, int count) {
        super(ARMADILLO_SCUTE, meta, count, "Armadillo Scute");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_80;
    }
}

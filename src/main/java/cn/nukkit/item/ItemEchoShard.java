package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemEchoShard extends Item {

    public ItemEchoShard() {
        this(0, 1);
    }

    public ItemEchoShard(Integer meta) {
        this(meta, 1);
    }

    public ItemEchoShard(Integer meta, int count) {
        super(ECHO_SHARD, meta, count, "Echo Shard");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_19_0_29;
    }
}

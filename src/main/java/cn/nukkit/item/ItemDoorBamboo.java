package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemDoorBamboo extends Item {

    public ItemDoorBamboo() {
        this(0, 1);
    }

    public ItemDoorBamboo(Integer meta) {
        this(meta, 1);
    }

    public ItemDoorBamboo(Integer meta, int count) {
        super(BAMBOO_DOOR, 0, count, "Bamboo Door");
        this.block = Block.get(BAMBOO_DOOR_BLOCK);
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0;
    }
}

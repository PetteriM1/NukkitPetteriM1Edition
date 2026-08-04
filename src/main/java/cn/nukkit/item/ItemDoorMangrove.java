package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemDoorMangrove extends Item {

    public ItemDoorMangrove() {
        this(0, 1);
    }

    public ItemDoorMangrove(Integer meta) {
        this(meta, 1);
    }

    public ItemDoorMangrove(Integer meta, int count) {
        super(MANGROVE_DOOR, 0, count, "Mangrove Door");
        this.block = Block.get(MANGROVE_DOOR_BLOCK);
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_19_0;
    }
}

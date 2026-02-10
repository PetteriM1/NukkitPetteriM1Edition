package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemDoorCherry extends Item {

    public ItemDoorCherry() {
        this(0, 1);
    }

    public ItemDoorCherry(Integer meta) {
        this(meta, 1);
    }

    public ItemDoorCherry(Integer meta, int count) {
        super(CHERRY_DOOR, 0, count, "Cherry Door");
        this.block = Block.get(CHERRY_DOOR_BLOCK);
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0;
    }
}

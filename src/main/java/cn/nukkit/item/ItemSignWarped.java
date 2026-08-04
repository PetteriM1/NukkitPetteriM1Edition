package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemSignWarped extends Item {

    public ItemSignWarped() {
        this(0, 1);
    }

    public ItemSignWarped(Integer meta) {
        this(meta, 1);
    }

    public ItemSignWarped(Integer meta, int count) {
        super(WARPED_SIGN, 0, count, "Warped Sign");
        this.block = Block.get(BlockID.WARPED_STANDING_SIGN);
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_16_0;
    }
}

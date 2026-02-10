package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemNetherSprouts extends Item {

    public ItemNetherSprouts() {
        this(0, 1);
    }

    public ItemNetherSprouts(Integer meta) {
        this(meta, 1);
    }

    public ItemNetherSprouts(Integer meta, int count) {
        super(NETHER_SPROUTS, 0, count, "Nether Sprouts");
        this.block = Block.get(BlockID.NETHER_SPROUTS_BLOCK);
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_16_0;
    }
}

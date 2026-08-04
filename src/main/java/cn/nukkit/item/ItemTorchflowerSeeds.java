package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemTorchflowerSeeds extends Item {

    public ItemTorchflowerSeeds() {
        this(0, 1);
    }

    public ItemTorchflowerSeeds(Integer meta) {
        this(meta, 1);
    }

    public ItemTorchflowerSeeds(Integer meta, int count) {
        super(TORCHFLOWER_SEEDS, meta, count, "Torchflower Seeds");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

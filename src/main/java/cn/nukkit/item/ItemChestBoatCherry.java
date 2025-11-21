package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemChestBoatCherry extends ItemChestBoat {

    public ItemChestBoatCherry() {
        this(0, 1);
    }

    public ItemChestBoatCherry(Integer meta) {
        this(meta, 1);
    }

    public ItemChestBoatCherry(Integer meta, int count) {
        super(CHERRY_CHEST_BOAT, meta, count, "Cherry Boat with Chest");
    }

    @Override
    public int getVariant() {
        return 8;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

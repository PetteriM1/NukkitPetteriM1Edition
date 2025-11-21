package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemIngotCopper extends Item implements ItemTrimMaterial {

    public ItemIngotCopper() {
        this(0, 1);
    }

    public ItemIngotCopper(Integer meta) {
        this(meta, 1);
    }

    public ItemIngotCopper(Integer meta, int count) {
        super(COPPER_INGOT, 0, count, "Copper Ingot");
    }

    @Override
    public ItemTrimMaterial.Type getMaterial() {
        return ItemTrimMaterial.Type.COPPER;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_17_0;
    }
}

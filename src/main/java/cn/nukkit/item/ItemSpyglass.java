package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * @author LT_Name
 */
public class ItemSpyglass extends Item {

    public ItemSpyglass() {
        this(0, 1);
    }

    public ItemSpyglass(Integer meta) {
        this(meta, 1);
    }

    public ItemSpyglass(Integer meta, int count) {
        super(SPYGLASS, 0, count, "Spyglass");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_17_0;
    }
}

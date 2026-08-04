package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * @author Kaooot
 */
public class ItemHoneycomb extends Item {

    public ItemHoneycomb() {
        this(0, 1);
    }

    public ItemHoneycomb(Integer meta) {
        this(meta, 1);
    }

    public ItemHoneycomb(Integer meta, int count) {
        super(HONEYCOMB, meta, count, "Honeycomb");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_14_0;
    }
}

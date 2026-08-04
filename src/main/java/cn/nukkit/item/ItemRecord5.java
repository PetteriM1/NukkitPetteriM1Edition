package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRecord5 extends ItemRecord {

    public ItemRecord5() {
        this(0, 1);
    }

    public ItemRecord5(Integer meta) {
        this(meta, 1);
    }

    public ItemRecord5(Integer meta, int count) {
        super(RECORD_5, meta, count);
    }

    @Override
    public String getSoundId() {
        return "record.5";
    }

    @Override
    public String getDiscName() {
        return "Samuel Åberg - 5";
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_19_0_29;
    }
}

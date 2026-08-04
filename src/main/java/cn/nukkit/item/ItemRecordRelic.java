package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRecordRelic extends ItemRecord {

    public ItemRecordRelic() {
        this(0, 1);
    }

    public ItemRecordRelic(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordRelic(Integer meta, int count) {
        super(RECORD_RELIC, meta, count);
    }

    @Override
    public String getSoundId() {
        return "record.relic";
    }

    @Override
    public String getDiscName() {
        return "Aaron Cherof - Relic";
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_0_23;
    }
}

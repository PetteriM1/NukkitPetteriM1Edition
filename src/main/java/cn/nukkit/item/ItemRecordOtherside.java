package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRecordOtherside extends ItemRecord {

    public ItemRecordOtherside() {
        this(0, 1);
    }

    public ItemRecordOtherside(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordOtherside(Integer meta, int count) {
        super(RECORD_OTHERSIDE, meta, count);
    }

    @Override
    public String getSoundId() {
        return "record.otherside";
    }

    @Override
    public String getDiscName() {
        return "Lena Raine - otherside";
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_18_0_22;
    }
}

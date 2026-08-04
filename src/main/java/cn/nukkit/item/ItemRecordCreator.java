package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRecordCreator extends ItemRecord {

    public ItemRecordCreator() {
        this(0, 1);
    }

    public ItemRecordCreator(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordCreator(Integer meta, int count) {
        super(RECORD_CREATOR, meta, count);
    }

    @Override
    public String getSoundId() {
        return "record.creator";
    }

    @Override
    public String getDiscName() {
        return "Lena Raine - Creator";
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}

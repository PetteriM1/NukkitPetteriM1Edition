package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRecordPigstep extends ItemRecord {

    public ItemRecordPigstep() {
        this(0, 1);
    }

    public ItemRecordPigstep(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordPigstep(Integer meta, int count) {
        super(RECORD_PIGSTEP, meta, count);
    }

    @Override
    public String getDiscName() {
        return "Lena Raine - Pigstep";
    }

    @Override
    public String getSoundId() {
        return "record.pigstep";
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_16_0;
    }
}

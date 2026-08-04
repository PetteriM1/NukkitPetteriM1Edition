package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRecordPrecipice extends ItemRecord {

    public ItemRecordPrecipice() {
        this(0, 1);
    }

    public ItemRecordPrecipice(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordPrecipice(Integer meta, int count) {
        super(RECORD_PRECIPICE, meta, count);
    }

    @Override
    public String getSoundId() {
        return "record.precipice";
    }

    @Override
    public String getDiscName() {
        return "Aaron Cherof - Precipice";
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}

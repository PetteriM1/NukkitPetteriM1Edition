package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRecordCreatorMusicBox extends ItemRecord {

    public ItemRecordCreatorMusicBox() {
        this(0, 1);
    }

    public ItemRecordCreatorMusicBox(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordCreatorMusicBox(Integer meta, int count) {
        super(RECORD_CREATOR_MUSIC_BOX, meta, count);
    }

    @Override
    public String getDiscName() {
        return "Lena Raine - Creator (Music Box)";
    }

    @Override
    public String getSoundId() {
        return "record.creator_music_box";
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}

package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

public class CreativeContentPacket extends DataPacket {

    public Item[] entries;

    @Override
    public byte pid() {
        return ProtocolInfo.CREATIVE_CONTENT_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(entries.length);
        int i = 1; //HACK around since 0 is not indexed by client
        for (Item entry : entries) {
            this.putUnsignedVarInt(i++);
            this.putSlot(protocol, entry);
        }
    }
}

package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class TakeItemEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.TAKE_ITEM_ENTITY_PACKET;

    public long entityId;
    public long target;

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.target);
        this.putEntityRuntimeId(this.entityId);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

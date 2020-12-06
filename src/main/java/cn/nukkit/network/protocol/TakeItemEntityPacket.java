package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class TakeItemEntityPacket extends DataPacket {

    public long entityId;
    public long target;

    @Override
    public void decode(int protocolId) {
        this.target = this.getEntityRuntimeId();
        this.entityId = this.getEntityRuntimeId();
    }

    @Override
    public void encode(int protocolId) {
        this.putEntityRuntimeId(this.target);
        this.putEntityRuntimeId(this.entityId);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.TAKE_ITEM_ENTITY_PACKET;
    }
}

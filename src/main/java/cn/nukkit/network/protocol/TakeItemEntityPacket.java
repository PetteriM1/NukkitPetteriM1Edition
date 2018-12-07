package cn.nukkit.network.protocol;

public class TakeItemEntityPacket extends DataPacket {

    public long entityId;
    public long target;

    @Override
    public void decode() {
        this.target = this.getEntityRuntimeId();
        this.entityId = this.getEntityRuntimeId();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.target);
        this.putEntityRuntimeId(this.entityId);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.TAKE_ITEM_ENTITY_PACKET;
    }
}

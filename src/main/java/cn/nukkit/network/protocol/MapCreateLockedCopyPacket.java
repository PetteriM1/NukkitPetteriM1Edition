package cn.nukkit.network.protocol;

public class MapCreateLockedCopyPacket extends DataPacket {

    public long originalMapId;
    public long newMapId;

    @Override
    public byte pid() {
        return ProtocolInfo.MAP_CREATE_LOCKED_COPY_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.originalMapId = this.getVarLong();
        this.newMapId = this.getVarLong();
    }

    @Override
    public void encode(int protocolId) {
        this.putVarLong(this.originalMapId);
        this.putVarLong(this.newMapId);
    }
}

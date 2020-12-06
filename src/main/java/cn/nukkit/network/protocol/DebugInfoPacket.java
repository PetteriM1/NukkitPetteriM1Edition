package cn.nukkit.network.protocol;

public class DebugInfoPacket extends DataPacket {

    public long entityId;
    public String data;

    @Override
    public byte pid() {
        return ProtocolInfo.DEBUG_INFO_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.entityId = this.getLong();
        this.data = this.getString();
    }

    @Override
    public void encode(int protocolId) {
        this.putLong(this.entityId);
        this.putString(this.data);
    }
}

package cn.nukkit.network.protocol;

public class ClientCacheStatusPacket extends DataPacket {

    public boolean supported;

    @Override
    public byte pid() {
        return ProtocolInfo.CLIENT_CACHE_STATUS_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.supported = this.getBoolean();
    }

    @Override
    public void encode(int protocolId) {
        this.putBoolean(this.supported);
    }
}

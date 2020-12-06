package cn.nukkit.network.protocol;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BatchPacket extends DataPacket {

    public byte[] payload;

    @Override
    public byte pid() {
        return ProtocolInfo.BATCH_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.payload = this.get();
    }

    @Override
    public void encode(int protocolId) {
    }

    public void trim() {
        setBuffer(null);
    }
}

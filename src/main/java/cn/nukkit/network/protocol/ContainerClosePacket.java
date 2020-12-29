package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class ContainerClosePacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.CONTAINER_CLOSE_PACKET;
    }

    public int windowId;
    public boolean wasServerInitiated;

    @Override
    public void decodePayload(int protocolId) {
        this.windowId = (byte) this.getByte();
        if (protocolId >= ProtocolInfo.v1_16_100) {
            this.wasServerInitiated = this.getBoolean();
        }
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putByte((byte) this.windowId);
        if (protocolId >= ProtocolInfo.v1_16_100) {
            this.putBoolean(this.wasServerInitiated);
        }
    }
}

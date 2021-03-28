package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class ContainerClosePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CONTAINER_CLOSE_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int windowId;
    public boolean wasServerInitiated;

    @Override
    public void decode() {
        this.windowId = (byte) this.getByte();
        if (protocol >= ProtocolInfo.v1_16_100) {
            this.wasServerInitiated = this.getBoolean();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.windowId);
        if (protocol >= ProtocolInfo.v1_16_100) {
            this.putBoolean(this.wasServerInitiated);
        }
    }
}

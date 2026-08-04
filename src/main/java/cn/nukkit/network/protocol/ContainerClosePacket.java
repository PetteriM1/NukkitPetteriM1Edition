package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class ContainerClosePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CONTAINER_CLOSE_PACKET;
    public int windowId;
    public int type;
    public boolean wasServerInitiated = true;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.windowId = (byte) this.getByte();
        if (protocol >= ProtocolInfo.v1_16_100) {
            if (protocol >= ProtocolInfo.v1_21_0) {
                this.type = (byte) this.getByte();
            }
            this.wasServerInitiated = this.getBoolean();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.windowId);
        if (protocol >= ProtocolInfo.v1_16_100) {
            if (protocol >= ProtocolInfo.v1_21_0) {
                this.putByte((byte) this.type);
            }
            this.putBoolean(this.wasServerInitiated);
        }
    }
}

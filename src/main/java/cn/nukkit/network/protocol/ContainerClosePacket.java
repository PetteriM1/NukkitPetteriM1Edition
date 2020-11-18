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
    public void decode() {
        this.windowId = (byte) this.getByte();
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

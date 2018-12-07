package cn.nukkit.network.protocol;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class ContainerClosePacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.CONTAINER_CLOSE_PACKET;
    }

    public int windowId;

    @Override
    public void decode() {
        this.windowId = this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.windowId);
    }
}

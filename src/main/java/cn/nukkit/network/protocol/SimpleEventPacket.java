package cn.nukkit.network.protocol;

public class SimpleEventPacket extends DataPacket {

    public static final int TYPE_ENABLE_COMMANDS = 1;
    public static final int TYPE_DISABLE_COMMANDS = 2;

    public short eventType;

    @Override
    public byte pid() {
        return ProtocolInfo.SIMPLE_EVENT_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putShort(this.eventType);
    }
}

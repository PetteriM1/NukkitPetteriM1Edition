package cn.nukkit.network.protocol;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class SetTimePacket extends DataPacket {

    public int time;

    @Override
    public byte pid() {
        return ProtocolInfo.SET_TIME_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.time);
    }
}

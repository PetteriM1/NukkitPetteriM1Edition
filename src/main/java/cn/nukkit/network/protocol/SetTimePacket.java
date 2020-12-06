package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class SetTimePacket extends DataPacket {

    public int time;

    @Override
    public byte pid() {
        return ProtocolInfo.SET_TIME_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putVarInt(this.time);
    }
}

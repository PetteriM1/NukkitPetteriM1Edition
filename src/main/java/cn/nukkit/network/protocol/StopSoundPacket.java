package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class StopSoundPacket extends DataPacket {

    public String name;
    public boolean stopAll;

    @Override
    public byte pid() {
        return ProtocolInfo.STOP_SOUND_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putString(this.name);
        this.putBoolean(this.stopAll);
    }
}

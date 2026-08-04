package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class StopSoundPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.STOP_SOUND_PACKET;

    public String name;
    public boolean stopAll;
    public boolean stopMusicLegacy;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.name);
        this.putBoolean(this.stopAll);
        if (this.protocol >= ProtocolInfo.v1_21_20) {
            this.putBoolean(this.stopMusicLegacy);
        }
    }
}

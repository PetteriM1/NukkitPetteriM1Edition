package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class PlaySoundPacket extends DataPacket {

    public String name;
    public int x;
    public int y;
    public int z;
    public float volume;
    public float pitch;

    @Override
    public byte pid() {
        return ProtocolInfo.PLAY_SOUND_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.name);
        this.putBlockVector3(this.x << 3, this.y << 3, this.z << 3);
        this.putLFloat(this.volume);
        this.putLFloat(this.pitch);
    }
}

package cn.nukkit.network.protocol;

public class OnScreenTextureAnimationPacket extends DataPacket {

    public int effectId;

    @Override
    public byte pid() {
        return ProtocolInfo.ON_SCREEN_TEXTURE_ANIMATION_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.effectId = this.getLInt();
    }

    @Override
    public void encode(int protocolId) {
        this.putLInt(this.effectId);
    }
}

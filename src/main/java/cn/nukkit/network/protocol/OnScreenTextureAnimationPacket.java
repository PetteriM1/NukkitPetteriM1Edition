package cn.nukkit.network.protocol;

public class OnScreenTextureAnimationPacket extends DataPacket {

    public int effectId;

    @Override
    public byte pid() {
        return ProtocolInfo.ON_SCREEN_TEXTURE_ANIMATION_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        this.effectId = this.getLInt();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putLInt(this.effectId);
    }
}

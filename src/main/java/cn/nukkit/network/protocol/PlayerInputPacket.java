package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class PlayerInputPacket extends DataPacket {

    public float motionX;
    public float motionY;

    public boolean jumping;
    public boolean sneaking;

    @Override
    public void decode(int protocolId) {
        this.motionX = this.getLFloat();
        this.motionY = this.getLFloat();
        this.jumping = this.getBoolean();
        this.sneaking = this.getBoolean();
    }

    @Override
    public void encode(int protocolId) {
    }

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_INPUT_PACKET;
    }
}

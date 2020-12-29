package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class HurtArmorPacket extends DataPacket {

    public int cause;
    public int damage;

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        if (protocolId >= 407) {
            this.putVarInt(this.cause);
        }
        this.putVarInt(this.damage);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.HURT_ARMOR_PACKET;
    }
}

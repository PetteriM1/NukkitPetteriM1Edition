package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class HurtArmorPacket extends DataPacket {

    public int health;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.health);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.HURT_ARMOR_PACKET;
    }
}

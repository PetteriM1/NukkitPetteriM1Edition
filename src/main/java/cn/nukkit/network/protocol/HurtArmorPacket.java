package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class HurtArmorPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.HURT_ARMOR_PACKET;

    public int cause;
    public int damage;
    public long armorSlots;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        if (protocol >= ProtocolInfo.v1_16_0) {
            this.putVarInt(this.cause);
        }
        this.putVarInt(this.damage);
        if (protocol >= ProtocolInfo.v1_17_30) {
            this.putUnsignedVarLong(this.armorSlots);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

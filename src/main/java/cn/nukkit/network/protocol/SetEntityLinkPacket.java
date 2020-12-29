package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class SetEntityLinkPacket extends DataPacket {

    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDE = 1;
    public static final byte TYPE_PASSENGER = 2;

    public long vehicleUniqueId;
    public long riderUniqueId;
    public byte type;
    public byte immediate;
    public boolean riderInitiated = false;

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putEntityUniqueId(this.vehicleUniqueId);
        this.putEntityUniqueId(this.riderUniqueId);
        this.putByte(this.type);
        this.putByte(this.immediate);
        if (protocolId >= 407) {
            this.putBoolean(this.riderInitiated);
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SET_ENTITY_LINK_PACKET;
    }
}

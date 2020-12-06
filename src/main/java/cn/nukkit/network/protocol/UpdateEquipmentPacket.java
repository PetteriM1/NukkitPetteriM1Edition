package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString(exclude = "namedtag")
public class UpdateEquipmentPacket extends DataPacket {

    public int windowId;
    public int windowType;
    public int unknown;
    public long eid;
    public byte[] namedtag;


    @Override
    public byte pid() {
        return ProtocolInfo.UPDATE_EQUIPMENT_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putByte((byte) this.windowId);
        this.putByte((byte) this.windowType);
        this.putEntityUniqueId(this.eid);
        this.put(this.namedtag);
    }
}

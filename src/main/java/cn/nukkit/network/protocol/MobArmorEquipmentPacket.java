package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class MobArmorEquipmentPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;
    }

    public long eid;
    public Item[] slots = new Item[4];

    @Override
    public void decodePayload(int protocolId) {
        this.eid = this.getEntityRuntimeId();
        this.slots = new Item[4];
        this.slots[0] = this.getSlot(protocolId);
        this.slots[1] = this.getSlot(protocolId);
        this.slots[2] = this.getSlot(protocolId);
        this.slots[3] = this.getSlot(protocolId);
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putEntityRuntimeId(this.eid);
        this.putSlot(protocolId, this.slots[0]);
        this.putSlot(protocolId, this.slots[1]);
        this.putSlot(protocolId, this.slots[2]);
        this.putSlot(protocolId, this.slots[3]);
    }
}

package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class MobEquipmentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOB_EQUIPMENT_PACKET;
    public long eid;
    public Item item;
    public int inventorySlot;
    public int hotbarSlot;
    public int windowId;

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();

        if (Server.getInstance().suomiCraftPEMode()) {
            this.getDummyNetworkItemStackDescriptor(this.protocol);
        } else {
            this.item = this.getNetworkItemStackDescriptor(this.protocol);
        }

        this.inventorySlot = this.getByte();
        this.hotbarSlot = this.getByte();
        this.windowId = this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putNetworkItemStackDescriptor(protocol, this.item);
        this.putByte((byte) this.inventorySlot);
        this.putByte((byte) this.hotbarSlot);
        this.putByte((byte) this.windowId);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

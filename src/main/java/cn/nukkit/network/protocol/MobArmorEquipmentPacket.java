package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class MobArmorEquipmentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;
    private static final Item AIR = Item.get(Item.AIR);
    public long eid;
    public Item[] slots = new Item[4];
    public Item body = AIR;

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        this.slots = new Item[4];

        if (protocol >= ProtocolInfo.v1_26_30) {
            this.slots[0] = this.getNetworkItemStackDescriptor(this.protocol);
            this.slots[1] = this.getNetworkItemStackDescriptor(this.protocol);
            this.slots[2] = this.getNetworkItemStackDescriptor(this.protocol);
            this.slots[3] = this.getNetworkItemStackDescriptor(this.protocol);

            if (Server.getInstance().suomiCraftPEMode()) {
                this.getDummyNetworkItemStackDescriptor(this.protocol);
                this.body = AIR;
            } else {
                this.body = this.getNetworkItemStackDescriptor(this.protocol);
            }
        } else {
            this.slots[0] = this.getSlot(this.protocol);
            this.slots[1] = this.getSlot(this.protocol);
            this.slots[2] = this.getSlot(this.protocol);
            this.slots[3] = this.getSlot(this.protocol);

            if (this.protocol >= ProtocolInfo.v1_21_20) {
                if (Server.getInstance().suomiCraftPEMode()) {
                    this.getDummySlot(this.protocol);
                    this.body = AIR;
                } else {
                    this.body = this.getSlot(this.protocol);
                }
            }
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);

        if (protocol >= ProtocolInfo.v1_26_30) {
            this.putNetworkItemStackDescriptor(protocol, this.slots[0]);
            this.putNetworkItemStackDescriptor(protocol, this.slots[1]);
            this.putNetworkItemStackDescriptor(protocol, this.slots[2]);
            this.putNetworkItemStackDescriptor(protocol, this.slots[3]);
            this.putNetworkItemStackDescriptor(protocol, this.body);
        } else {
            this.putSlot(protocol, this.slots[0]);
            this.putSlot(protocol, this.slots[1]);
            this.putSlot(protocol, this.slots[2]);
            this.putSlot(protocol, this.slots[3]);

            if (this.protocol >= ProtocolInfo.v1_21_20) {
                this.putSlot(protocol, this.body);
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

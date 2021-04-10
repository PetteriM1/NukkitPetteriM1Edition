package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class InventorySlotPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.INVENTORY_SLOT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int inventoryId;
    public int networkId;
    public int slot;
    public Item item;

    @Override
    public void decode() {
        this.inventoryId = (int) this.getUnsignedVarInt();
        this.slot = (int) this.getUnsignedVarInt();
        this.item = this.getSlot(this.protocol);
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.inventoryId);
        this.putUnsignedVarInt(this.slot);
        if (protocol >= 407 && protocol < ProtocolInfo.v1_16_220) {
            this.putVarInt(networkId);
        }
        this.putSlot(protocol, this.item);
    }
}

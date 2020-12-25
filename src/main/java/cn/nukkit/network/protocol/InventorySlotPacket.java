package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class InventorySlotPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.INVENTORY_SLOT_PACKET;
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
        if (protocol >= 407) {
            this.putVarInt(networkId);
        }
        this.putSlot(protocol, this.item);
    }
}

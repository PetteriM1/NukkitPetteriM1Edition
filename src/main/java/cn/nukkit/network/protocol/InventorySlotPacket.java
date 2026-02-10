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
    private static final Item EMPTY_STORAGE_ITEM = Item.get(Item.AIR);
    public int inventoryId;
    public int networkId;
    public int slot;
    public Item item;

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.inventoryId);
        this.putUnsignedVarInt(this.slot);
        if (protocol >= ProtocolInfo.v1_21_30) {
            this.putByte((byte) 0); // fullContainerName.id
            this.putBoolean(false); // fullContainerName.optional.present
            if (protocol >= ProtocolInfo.v1_21_40) {
                this.putSlot(protocol, EMPTY_STORAGE_ITEM);
            } else {
                this.putUnsignedVarInt(0); // dynamicContainerSize
            }
        } else if (protocol >= ProtocolInfo.v1_21_20) {
            this.putUnsignedVarInt(0); // dynamicContainerId
        }
        if (protocol >= 407 && protocol < ProtocolInfo.v1_16_220) {
            this.putVarInt(this.networkId);
        }
        this.putSlot(protocol, this.item);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

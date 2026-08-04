package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class InventoryContentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.INVENTORY_CONTENT_PACKET;
    public static final int SPECIAL_INVENTORY = 0;
    public static final int SPECIAL_OFFHAND = 0x77;
    public static final int SPECIAL_ARMOR = 0x78;
    public static final int SPECIAL_CREATIVE = 0x79;
    public static final int SPECIAL_HOTBAR = 0x7a;
    public static final int SPECIAL_FIXED_INVENTORY = 0x7b;
    private static final Item EMPTY_STORAGE_ITEM = Item.get(Item.AIR);
    public int inventoryId;
    public Item[] slots = new Item[0];

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public DataPacket clean() {
        this.slots = new Item[0];
        return super.clean();
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.inventoryId);
        this.putUnsignedVarInt(this.slots.length);
        for (Item slot : this.slots) {
            if (protocol >= 407 && protocol < ProtocolInfo.v1_16_220) {
                this.putVarInt(0);
            }
            if (protocol >= ProtocolInfo.v1_26_30) {
                this.putNetworkItemStackDescriptor(protocol, slot);
            } else {
                this.putSlot(protocol, slot);
            }
        }
        if (protocol >= ProtocolInfo.v1_21_30) {
            this.putByte((byte) 0); // fullContainerName.id
            this.putBoolean(false); // fullContainerName.optional.present
            if (protocol >= ProtocolInfo.v1_21_40) {
                if (protocol >= ProtocolInfo.v1_26_30) {
                    this.putNetworkItemStackDescriptor(protocol, EMPTY_STORAGE_ITEM);
                } else {
                    this.putSlot(protocol, EMPTY_STORAGE_ITEM);
                }
            } else {
                this.putUnsignedVarInt(0); // dynamicContainerSize
            }
        } else if (protocol >= ProtocolInfo.v1_21_20) {
            this.putUnsignedVarInt(0); // dynamicContainerId
        }
    }
}

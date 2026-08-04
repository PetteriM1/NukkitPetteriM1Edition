package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@ToString
public class CreativeContentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CREATIVE_CONTENT_PACKET;

    public Item.CreativeItems creativeItems;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();

        if (this.creativeItems == null) { // Spectator
            if (protocol >= ProtocolInfo.v1_21_60) {
                this.putUnsignedVarInt(0);
            }
            this.putUnsignedVarInt(0);
            return;
        }

        if (protocol >= ProtocolInfo.v1_21_60) {
            List<Item.CreativeItemGroup> groups = creativeItems.getGroups();
            this.putUnsignedVarInt(groups.size());
            for (Item.CreativeItemGroup group : groups) {
                if (protocol >= ProtocolInfo.v1_26_40) {
                    this.putByte((byte) group.getCategory().ordinal());
                } else {
                    this.putLInt(group.getCategory().ordinal());
                }
                this.putString(group.getName());
                this.putSlot(protocol, group.getIcon(), true);
            }
        }

        int creativeNetId = 1; // 0 is not indexed by client

        if (protocol >= ProtocolInfo.v1_21_60) {
            Map<Item, Item.CreativeItemGroup> contents = creativeItems.getContents(protocol);
            this.putUnsignedVarInt(contents.size());
            for (Map.Entry<Item, Item.CreativeItemGroup> entry : contents.entrySet()) {
                this.putUnsignedVarInt(creativeNetId++);
                this.putSlot(protocol, entry.getKey(), true);
                this.putUnsignedVarInt(entry.getValue().getGroupId());
            }
        } else {
            Collection<Item> items = creativeItems.getItems(protocol);
            this.putUnsignedVarInt(items.size());
            for (Item entry : items) {
                this.putUnsignedVarInt(creativeNetId++);
                this.putSlot(protocol, entry, protocol >= ProtocolInfo.v1_16_220);
            }
        }
    }
}

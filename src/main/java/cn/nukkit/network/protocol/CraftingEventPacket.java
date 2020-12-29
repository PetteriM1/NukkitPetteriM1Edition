package cn.nukkit.network.protocol;


import cn.nukkit.item.Item;
import lombok.ToString;

import java.util.UUID;

/**
 * @author Nukkit Project Team
 */
@ToString
public class CraftingEventPacket extends DataPacket {

    public static final int TYPE_SHAPELESS = 0;
    public static final int TYPE_SHAPED = 1;
    public static final int TYPE_FURNACE = 2;
    public static final int TYPE_FURNACE_DATA = 3;
    public static final int TYPE_MULTI = 4;
    public static final int TYPE_SHULKER_BOX = 5;

    public int windowId;
    public int type;
    public UUID id;

    public Item[] input;
    public Item[] output;

    @Override
    public void decodePayload(int protocolId) {
        this.windowId = this.getByte();
        this.type = (int) this.getUnsignedVarInt();
        this.id = this.getUUID();

        int inputSize = (int) this.getUnsignedVarInt();
        this.input = new Item[Math.min(inputSize, 128)];
        for (int i = 0; i < this.input.length; ++i) {
            this.input[i] = this.getSlot(protocolId);
        }

        int outputSize = (int) this.getUnsignedVarInt();
        this.output = new Item[Math.min(outputSize, 128)];
        for (int i = 0; i < this.output.length; ++i) {
            this.output[i] = this.getSlot(protocolId);
        }
    }

    @Override
    public void encodePayload(int protocolId) {
    }

    @Override
    public byte pid() {
        return ProtocolInfo.CRAFTING_EVENT_PACKET;
    }
}

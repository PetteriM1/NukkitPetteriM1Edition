package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.ContainerIds;
import lombok.ToString;

@ToString
public class PlayerHotbarPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_HOTBAR_PACKET;

    public int selectedHotbarSlot;
    public int windowId = ContainerIds.INVENTORY;
    public boolean selectHotbarSlot = true;

    @Override
    public void decode() {
        this.selectedHotbarSlot = (int) this.getUnsignedVarInt();
        this.windowId = this.getByte();
        if (protocol <= 201) {
            int slotCount = (int) this.getUnsignedVarInt();
            if (slotCount > 1000) {
                throw new RuntimeException("Too many slots in one packet");
            }
            for (int i = 0; i < slotCount; ++i) {
                this.getUnsignedVarInt();
            }
        }
        this.selectHotbarSlot = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.selectedHotbarSlot);
        this.putByte((byte) this.windowId);
        if (protocol <= 201) {
            this.putUnsignedVarInt(0);
        }
        this.putBoolean(this.selectHotbarSlot);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

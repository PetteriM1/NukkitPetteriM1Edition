package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.ContainerIds;
import lombok.ToString;

@ToString
public class PlayerHotbarPacket extends DataPacket {

    public int selectedHotbarSlot;
    public int windowId = ContainerIds.INVENTORY;
    public boolean selectHotbarSlot = true;

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_HOTBAR_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        this.selectedHotbarSlot = (int) this.getUnsignedVarInt();
        this.windowId = this.getByte();
        this.selectHotbarSlot = this.getBoolean();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putUnsignedVarInt(this.selectedHotbarSlot);
        this.putByte((byte) this.windowId);
        if (protocolId == 201) {
            this.putUnsignedVarInt(0);
        }
        this.putBoolean(this.selectHotbarSlot);
    }
}

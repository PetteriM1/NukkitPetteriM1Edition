package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class GUIDataPickItemPacket extends DataPacket {

    public int hotbarSlot;

    @Override
    public byte pid() {
        return ProtocolInfo.GUI_DATA_PICK_ITEM_PACKET;
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putLInt(this.hotbarSlot);
    }

    @Override
    public void decodePayload(int protocolId) {
        this.hotbarSlot = this.getLInt();
    }
}

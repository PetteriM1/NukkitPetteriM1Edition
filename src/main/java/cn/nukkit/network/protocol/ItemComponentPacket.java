package cn.nukkit.network.protocol;

public class ItemComponentPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.ITEM_COMPONENT_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVarInt(0); // Send empty array for now
    }
}

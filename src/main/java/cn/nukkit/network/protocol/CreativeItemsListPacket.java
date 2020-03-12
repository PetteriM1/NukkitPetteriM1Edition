package cn.nukkit.network.protocol;

public class CreativeItemsListPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.CREATIVE_ITEMS_LIST_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(0); //groups count
        this.putVarInt(0); //items count
    }
}

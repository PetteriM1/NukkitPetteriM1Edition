package cn.nukkit.network.protocol;

public class BiomeDefinitionListPacket extends DataPacket {

    public byte[] tag;

    @Override
    public byte pid() {
        return ProtocolInfo.BIOME_DEFINITION_LIST_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.put(tag);
    }
}

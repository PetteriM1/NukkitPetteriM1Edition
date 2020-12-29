package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class StructureBlockUpdatePacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
    }
}

package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class SetLocalPlayerAsInitializedPacket extends DataPacket {

    public long eid;

    @Override
    public byte pid() {
        return ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        this.eid = this.getUnsignedVarLong();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putUnsignedVarLong(eid);
    }
}

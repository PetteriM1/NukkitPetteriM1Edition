package cn.nukkit.network.protocol;

public class SetLocalPlayerAsInitializedPacket extends DataPacket {

    public long eid;

    @Override
    public byte pid() {
        return ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET;
    }

    @Override
    public void decode() {
        this.eid = this.getUnsignedVarLong();
    }

    @Override
    public void encode() {
        this.putUnsignedVarLong(eid);
    }
}

package cn.nukkit.network.protocol;

public class EmotePacket extends DataPacket {

    public long runtimeId;
    public String emoteID;
    public byte flags;

    @Override
    public byte pid() {
        return ProtocolInfo.EMOTE_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.runtimeId = this.getEntityUniqueId();
        this.emoteID = this.getString();
        this.flags = (byte) this.getByte();
    }

    @Override
    public void encode(int protocolId) {
        this.putEntityUniqueId(this.runtimeId);
        this.putString(this.emoteID);
        this.putByte(flags);
    }
}

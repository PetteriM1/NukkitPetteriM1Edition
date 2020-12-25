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
    public void decode() {
        this.runtimeId = this.getEntityRuntimeId();
        this.emoteID = this.getString();
        this.flags = (byte) this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.runtimeId);
        this.putString(this.emoteID);
        this.putByte(flags);
    }
}

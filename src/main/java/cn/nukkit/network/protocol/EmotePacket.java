package cn.nukkit.network.protocol;

public class EmotePacket extends DataPacket {

    public long entityRuntimeId;
    public String emoteId;
    public int flags;

    @Override
    public byte pid() {
        return ProtocolInfo.EMOTE_PACKET;
    }

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityUniqueId();
        this.emoteId = this.getString();
        this.flags = this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityRuntimeId);
        this.putString(this.emoteId);
        this.putByte((byte) this.flags);
    }
}

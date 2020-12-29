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
    public void decodePayload(int protocolId) {
        this.runtimeId = this.getEntityRuntimeId();
        this.emoteID = this.getString();
        this.flags = (byte) this.getByte();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putEntityRuntimeId(this.runtimeId);
        this.putString(this.emoteID);
        this.putByte(flags);
    }
}

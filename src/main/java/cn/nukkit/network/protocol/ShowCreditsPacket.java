package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ShowCreditsPacket extends DataPacket {

    public static final int STATUS_START_CREDITS = 0;
    public static final int STATUS_END_CREDITS = 1;

    public long eid;
    public int status;

    @Override
    public byte pid() {
        return ProtocolInfo.SHOW_CREDITS_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        this.eid = this.getEntityRuntimeId();
        this.status = this.getVarInt();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putEntityRuntimeId(this.eid);
        this.putVarInt(this.status);
    }
}

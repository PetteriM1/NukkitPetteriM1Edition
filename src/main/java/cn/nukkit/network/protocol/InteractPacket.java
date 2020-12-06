package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class InteractPacket extends DataPacket {

    public static final int ACTION_VEHICLE_EXIT = 3;
    public static final int ACTION_MOUSEOVER = 4;
    public static final int ACTION_OPEN_NPC = 5;
    public static final int ACTION_OPEN_INVENTORY = 6;

    public int action;
    public long target;

    @Override
    public void decode(int protocolId) {
        this.action = this.getByte();
        this.target = this.getEntityRuntimeId();
    }

    @Override
    public void encode(int protocolId) {
        this.putByte((byte) this.action);
        this.putEntityRuntimeId(this.target);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.INTERACT_PACKET;
    }
}

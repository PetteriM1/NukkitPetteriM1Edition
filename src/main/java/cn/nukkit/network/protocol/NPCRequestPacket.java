package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class NPCRequestPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.NPC_REQUEST_PACKET;

    public long entityRuntimeId;
    public RequestType requestType;
    public String commandString;
    public int actionType;
    public String sceneName;

    public enum RequestType {
        SET_ACTIONS,
        EXECUTE_ACTION,
        EXECUTE_CLOSING_COMMANDS,
        SET_NAME,
        SET_SKIN,
        SET_INTERACTION_TEXT
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.requestType = RequestType.values()[this.getByte()];
        this.commandString = this.getString();
        this.actionType = this.getByte();
        if (this.protocol >= ProtocolInfo.v1_17_10) {
            this.sceneName = this.getString();
        }
    }

    @Override
    public void encode() {
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putByte((byte) requestType.ordinal());
        this.putString(this.commandString);
        this.putByte((byte) this.actionType);
        if (this.protocol >= ProtocolInfo.v1_17_10) {
            this.putString(this.sceneName);
        }
    }
}

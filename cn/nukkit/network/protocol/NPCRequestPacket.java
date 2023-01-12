/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class NPCRequestPacket
extends DataPacket {
    public static final byte NETWORK_ID = 98;
    public long entityRuntimeId;
    public RequestType requestType;
    public String commandString;
    public int actionType;
    public String sceneName;

    @Override
    public byte pid() {
        return 98;
    }

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.requestType = RequestType.values()[this.getByte()];
        this.commandString = this.getString();
        this.actionType = this.getByte();
        if (this.protocol >= 448) {
            this.sceneName = this.getString();
        }
    }

    @Override
    public void encode() {
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putByte((byte)this.requestType.ordinal());
        this.putString(this.commandString);
        this.putByte((byte)this.actionType);
        if (this.protocol >= 448) {
            this.putString(this.sceneName);
        }
    }

    public String toString() {
        return "NPCRequestPacket(entityRuntimeId=" + this.entityRuntimeId + ", requestType=" + (Object)((Object)this.requestType) + ", commandString=" + this.commandString + ", actionType=" + this.actionType + ", sceneName=" + this.sceneName + ")";
    }

    public static enum RequestType {
        SET_ACTIONS,
        EXECUTE_ACTION,
        EXECUTE_CLOSING_COMMANDS,
        SET_NAME,
        SET_SKIN,
        SET_INTERACTION_TEXT;

    }
}


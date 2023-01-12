/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetTitlePacket
extends DataPacket {
    public static final byte NETWORK_ID = 88;
    public static final int TYPE_CLEAR = 0;
    public static final int TYPE_RESET = 1;
    public static final int TYPE_TITLE = 2;
    public static final int TYPE_SUBTITLE = 3;
    public static final int TYPE_ACTION_BAR = 4;
    public static final int TYPE_ANIMATION_TIMES = 5;
    public int type;
    public String text = "";
    public int fadeInTime = 0;
    public int stayTime = 0;
    public int fadeOutTime = 0;
    public String xuid = "";
    public String platformOnlineId = "";

    @Override
    public byte pid() {
        return 88;
    }

    @Override
    public void decode() {
        this.type = this.getVarInt();
        this.text = this.getString();
        this.fadeInTime = this.getVarInt();
        this.stayTime = this.getVarInt();
        this.fadeOutTime = this.getVarInt();
        if (this.protocol >= 448) {
            this.xuid = this.getString();
            this.platformOnlineId = this.getString();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.type);
        this.putString(this.text);
        this.putVarInt(this.fadeInTime);
        this.putVarInt(this.stayTime);
        this.putVarInt(this.fadeOutTime);
        if (this.protocol >= 448) {
            this.putString(this.xuid);
            this.putString(this.platformOnlineId);
        }
    }

    public String toString() {
        return "SetTitlePacket(type=" + this.type + ", text=" + this.text + ", fadeInTime=" + this.fadeInTime + ", stayTime=" + this.stayTime + ", fadeOutTime=" + this.fadeOutTime + ", xuid=" + this.xuid + ", platformOnlineId=" + this.platformOnlineId + ")";
    }
}


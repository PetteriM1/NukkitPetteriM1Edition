/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class TextPacket
extends DataPacket {
    public static final byte NETWORK_ID = 9;
    public static final byte TYPE_RAW = 0;
    public static final byte TYPE_CHAT = 1;
    public static final byte TYPE_TRANSLATION = 2;
    public static final byte TYPE_POPUP = 3;
    public static final byte TYPE_JUKEBOX_POPUP = 4;
    public static final byte TYPE_TIP = 5;
    public static final byte TYPE_SYSTEM = 6;
    public static final byte TYPE_WHISPER = 7;
    public static final byte TYPE_ANNOUNCEMENT = 8;
    public static final byte TYPE_OBJECT = 9;
    public static final byte TYPE_OBJECT_WHISPER = 10;
    public byte type;
    public String source = "";
    public String message = "";
    public String[] parameters = new String[0];
    public boolean isLocalized = false;
    public String xboxUserId = "";
    public String platformChatId = "";

    @Override
    public byte pid() {
        return 9;
    }

    @Override
    public void decode() {
        this.type = (byte)this.getByte();
        this.isLocalized = this.getBoolean() || this.type == 2;
        switch (this.type) {
            case 1: 
            case 7: 
            case 8: {
                this.source = this.getString();
                if (this.protocol > 201 && this.protocol <= 282) {
                    this.getString();
                    this.getVarInt();
                }
            }
            case 0: 
            case 5: 
            case 6: 
            case 9: 
            case 10: {
                this.message = this.getString();
                break;
            }
            case 2: 
            case 3: 
            case 4: {
                this.message = this.getString();
                int n = (int)this.getUnsignedVarInt();
                this.parameters = new String[Math.min(n, 128)];
                for (int k = 0; k < this.parameters.length; ++k) {
                    this.parameters[k] = this.getString();
                }
                break;
            }
        }
        if (this.protocol >= 223) {
            this.xboxUserId = this.getString();
            this.platformChatId = this.getString();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.type);
        this.putBoolean(this.isLocalized || this.type == 2);
        switch (this.type) {
            case 1: 
            case 7: 
            case 8: {
                this.putString(this.source);
                if (this.protocol > 201 && this.protocol <= 282) {
                    this.putString("");
                    this.putVarInt(0);
                }
            }
            case 0: 
            case 5: 
            case 6: 
            case 9: 
            case 10: {
                this.putString(this.message);
                break;
            }
            case 2: 
            case 3: 
            case 4: {
                this.putString(this.message);
                this.putUnsignedVarInt(this.parameters.length);
                for (String string : this.parameters) {
                    this.putString(string);
                }
                break;
            }
        }
        if (this.protocol >= 223) {
            this.putString(this.xboxUserId);
            this.putString(this.platformChatId);
        }
    }

    public String toString() {
        return "TextPacket(type=" + this.type + ", source=" + this.source + ", message=" + this.message + ", parameters=" + Arrays.deepToString(this.parameters) + ", isLocalized=" + this.isLocalized + ", xboxUserId=" + this.xboxUserId + ", platformChatId=" + this.platformChatId + ")";
    }
}


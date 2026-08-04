package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class TextPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.TEXT_PACKET;
    public static final byte TYPE_RAW = 0;
    public static final byte TYPE_CHAT = 1;
    public static final byte TYPE_TRANSLATION = 2;
    public static final byte TYPE_POPUP = 3;
    public static final byte TYPE_JUKEBOX_POPUP = 4;
    public static final byte TYPE_TIP = 5;
    public static final byte TYPE_SYSTEM = 6;
    public static final byte TYPE_WHISPER = 7;
    public static final byte TYPE_ANNOUNCEMENT = 8;
    public static final byte TYPE_OBJECT_WHISPER = 9;
    public static final byte TYPE_OBJECT = 10;
    public static final byte TYPE_OBJECT_ANNOUNCEMENT = 11;
    public byte type;
    public String source = "";
    public String message = "";
    public String[] parameters = new String[0];
    public boolean isLocalized = false;
    public String xboxUserId = "";
    public String platformChatId = "";
    public String filteredMessage = "";

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        if (protocol < ProtocolInfo.v1_21_130_28) {
            this.type = (byte) getByte();
        }

        this.isLocalized = this.getBoolean() || type == TYPE_TRANSLATION;

        if (protocol < ProtocolInfo.v1_21_130_28) {
            switch (type) {
                case TYPE_CHAT:
                case TYPE_WHISPER:
                case TYPE_ANNOUNCEMENT:
                    this.source = this.getString();
                    if (protocol > 201 && protocol <= 282) {
                        this.getString();
                        this.getVarInt();
                    }
                case TYPE_RAW:
                case TYPE_TIP:
                case TYPE_SYSTEM:
                case TYPE_OBJECT:
                case TYPE_OBJECT_WHISPER:
                case TYPE_OBJECT_ANNOUNCEMENT:
                    this.message = this.getString();
                    break;

                case TYPE_TRANSLATION:
                case TYPE_POPUP:
                case TYPE_JUKEBOX_POPUP:
                    this.message = this.getString();
                    int paramCount = (int) this.getUnsignedVarInt();
                    if (paramCount > 4) {
                        throw new IllegalArgumentException("Parameter List maxItems is 4");
                    }
                    this.parameters = new String[paramCount];
                    for (int i = 0; i < this.parameters.length; i++) {
                        this.parameters[i] = this.getString();
                    }
            }
        } else {
            switch (this.getByte()) {
                case 0: // MessageOnly
                    if (protocol < ProtocolInfo.v1_26_0) {
                        for (int i = 0; i < 6; i++) {
                            this.getString();
                        }
                    }
                    this.type = (byte) getByte();
                    this.message = this.getString();
                    break;
                case 1: // AuthorAndMessage
                    if (protocol < ProtocolInfo.v1_26_0) {
                        for (int i = 0; i < 3; i++) {
                            this.getString();
                        }
                    }
                    this.type = (byte) getByte();
                    this.source = this.getString();
                    this.message = this.getString();
                    break;
                case 2: // MessageAndParams
                    if (protocol < ProtocolInfo.v1_26_0) {
                        for (int i = 0; i < 3; i++) {
                            this.getString();
                        }
                    }
                    this.type = (byte) getByte();
                    this.message = this.getString();
                    int paramCount = (int) this.getUnsignedVarInt();
                    if (paramCount > 4) {
                        throw new IllegalArgumentException("Parameter List maxItems is 4");
                    }
                    this.parameters = new String[paramCount];
                    for (int i = 0; i < this.parameters.length; i++) {
                        this.parameters[i] = this.getString();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Not oneOf<MessageOnly, AuthorAndMessage, MessageAndParams>");
            }
        }

        if (protocol >= 223) {
            this.xboxUserId = this.getString();
            this.platformChatId = this.getString();

            if (protocol >= ProtocolInfo.v1_21_0 && (this.protocol < ProtocolInfo.v1_21_130_28 || this.getBoolean())) {
                this.filteredMessage = this.getString();
            }
        }
    }

    @Override
    public void encode() {
        this.reset();

        if (protocol < ProtocolInfo.v1_21_130_28) {
            this.putByte(this.type);
        }

        this.putBoolean(this.isLocalized || type == TYPE_TRANSLATION);

        if (protocol < ProtocolInfo.v1_21_130_28) {
            switch (this.type) {
                case TYPE_CHAT:
                case TYPE_WHISPER:
                case TYPE_ANNOUNCEMENT:
                    this.putString(this.source);
                    if (protocol > 201 && protocol <= 282) {
                        this.putString("");
                        this.putVarInt(0);
                    }
                case TYPE_RAW:
                case TYPE_TIP:
                case TYPE_SYSTEM:
                case TYPE_OBJECT:
                case TYPE_OBJECT_WHISPER:
                case TYPE_OBJECT_ANNOUNCEMENT:
                    this.putString(this.message);
                    break;

                case TYPE_TRANSLATION:
                case TYPE_POPUP:
                case TYPE_JUKEBOX_POPUP:
                    this.putString(this.message);
                    this.putUnsignedVarInt(this.parameters.length);
                    for (String parameter : this.parameters) {
                        this.putString(parameter);
                    }
            }
        } else {
            // 1.21.130 doesn't allow empty messages
            if (this.message.isEmpty()) {
                this.message = " ";
            }

            switch (this.type) {
                case TYPE_RAW:
                case TYPE_TIP:
                case TYPE_SYSTEM:
                case TYPE_OBJECT:
                case TYPE_OBJECT_WHISPER:
                case TYPE_OBJECT_ANNOUNCEMENT:
                    this.putByte((byte) 0); // MessageOnly
                    if (protocol < ProtocolInfo.v1_26_0) {
                        this.putString("raw");
                        this.putString("tip");
                        this.putString("systemMessage");
                        this.putString("textObjectWhisper");
                        this.putString("textObjectAnnouncement");
                        this.putString("textObject");
                    }
                    this.putByte(this.type);
                    this.putString(this.message);
                    break;

                case TYPE_CHAT:
                case TYPE_WHISPER:
                case TYPE_ANNOUNCEMENT:
                    this.putByte((byte) 1); // AuthorAndMessage
                    if (protocol < ProtocolInfo.v1_26_0) {
                        this.putString("chat");
                        this.putString("whisper");
                        this.putString("announcement");
                    }
                    this.putByte(this.type);
                    this.putString(this.source);
                    this.putString(this.message);
                    break;

                case TYPE_TRANSLATION:
                case TYPE_POPUP:
                case TYPE_JUKEBOX_POPUP:
                    this.putByte((byte) 2); // MessageAndParams
                    if (protocol < ProtocolInfo.v1_26_0) {
                        this.putString("translate");
                        this.putString("popup");
                        this.putString("jukeboxPopup");
                    }
                    this.putByte(this.type);
                    this.putString(this.message);
                    this.putUnsignedVarInt(this.parameters.length);
                    for (String parameter : this.parameters) {
                        this.putString(parameter);
                    }
            }
        }

        if (protocol >= 223) {
            this.putString(this.xboxUserId);
            this.putString(this.platformChatId);

            if (protocol >= ProtocolInfo.v1_21_0) {
                if (protocol >= ProtocolInfo.v1_21_130_28) {
                    this.putBoolean(!this.filteredMessage.isEmpty());
                }
                if (protocol < ProtocolInfo.v1_21_130_28 || !this.filteredMessage.isEmpty()) {
                    this.putString(this.filteredMessage);
                }
            }
        }
    }
}

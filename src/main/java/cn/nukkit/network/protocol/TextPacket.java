package cn.nukkit.network.protocol;

public class TextPacket extends DataPacket {

    public boolean protocolLowerThan291 = false;

    @Override
    public byte pid() {
        return ProtocolInfo.TEXT_PACKET;
    }

    public TextPacket() {
        this(false);
    }

    public TextPacket(boolean protocolLowerThan291) {
        this.protocolLowerThan291 = protocolLowerThan291;
    }

    public static final byte TYPE_RAW = 0;
    public static final byte TYPE_CHAT = 1;
    public static final byte TYPE_TRANSLATION = 2;
    public static final byte TYPE_POPUP = 3;
    public static final byte TYPE_TIP = 4;
    public static final byte TYPE_SYSTEM = 5;
    public static final byte TYPE_WHISPER = 6;
    public static final byte TYPE_ANNOUNCEMENT = 7;

    public byte type;
    public String source = "";
    public String message = "";
    public String[] parameters = new String[0];
    public boolean isLocalized = false;
    public String platformChatId = "";

    @Override
    public void decode() {
        this.type = (byte) getByte();
        this.isLocalized = this.getBoolean() || type == TYPE_TRANSLATION;
        if (type == TYPE_CHAT) {
            this.source = this.getString();
            String value2 = this.getString();
            try {
                this.getVarInt();
            } catch (Exception e) {
                this.platformChatId = this.getString();
            }
            String value4 = this.getString();
            try {
                this.platformChatId = this.getString();
                this.message = value4;
            } catch (Exception e) {
                this.message = value2;
            }
        } else {
            switch (type) {
                case TYPE_POPUP:
                case TYPE_WHISPER:
                case TYPE_ANNOUNCEMENT:
                    this.source = this.getString();
                case TYPE_RAW:
                case TYPE_TIP:
                case TYPE_SYSTEM:
                    this.message = this.getString();
                    break;

                case TYPE_TRANSLATION:
                    this.message = this.getString();
                    int count = (int) this.getUnsignedVarInt();
                    this.parameters = new String[count];
                    for (int i = 0; i < count; i++) {
                        this.parameters[i] = this.getString();
                    }
            }
            this.platformChatId = this.getString();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.type);
        this.putBoolean(this.isLocalized || type == TYPE_TRANSLATION);
        switch (this.type) {
            case TYPE_POPUP:
            case TYPE_CHAT:
            case TYPE_WHISPER:
            case TYPE_ANNOUNCEMENT:
                this.putString(this.source);
                if (protocolLowerThan291) {
                    this.putString("");
                    this.putVarInt(0);
                }
            case TYPE_RAW:
            case TYPE_TIP:
            case TYPE_SYSTEM:
                this.putString(this.message);
                break;

            case TYPE_TRANSLATION:
                this.putString(this.message);
                this.putUnsignedVarInt(this.parameters.length);
                for (String parameter : this.parameters) {
                    this.putString(parameter);
                }
        }
        this.putString(this.platformChatId);
    }
}

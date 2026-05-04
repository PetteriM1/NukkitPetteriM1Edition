package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class DisconnectPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.DISCONNECT_PACKET;

    public boolean hideDisconnectionScreen;
    public String message;
    public String filteredMessage = "";

    @Override
    public void decode() {
        if (protocol >= ProtocolInfo.v1_20_40) {
            this.getVarInt(); // Disconnect fail reason
        }
        this.hideDisconnectionScreen = this.getBoolean();
        this.message = this.getString();
        if (this.protocol >= ProtocolInfo.v1_21_20) {
            this.filteredMessage = this.getString();
        }
    }

    @Override
    public void encode() {
        this.reset();
        if (protocol >= ProtocolInfo.v1_20_40) {
            this.putVarInt(0); // Disconnect fail reason UNKNOWN
        }
        this.putBoolean(this.hideDisconnectionScreen); // varuint32 since 1.26.20 but reads basically the same
        if (!this.hideDisconnectionScreen) {
            this.putString(this.message);
            if (this.protocol >= ProtocolInfo.v1_21_20) {
                this.putString(this.filteredMessage);
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

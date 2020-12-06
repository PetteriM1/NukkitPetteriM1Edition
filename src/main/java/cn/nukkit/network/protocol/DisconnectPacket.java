package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class DisconnectPacket extends DataPacket {

    public boolean hideDisconnectionScreen = false;
    public String message;

    @Override
    public byte pid() {
        return ProtocolInfo.DISCONNECT_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.hideDisconnectionScreen = this.getBoolean();
        this.message = this.getString();
    }

    @Override
    public void encode(int protocolId) {
        this.putBoolean(this.hideDisconnectionScreen);
        if (!this.hideDisconnectionScreen) {
            this.putString(this.message);
        }
    }
}

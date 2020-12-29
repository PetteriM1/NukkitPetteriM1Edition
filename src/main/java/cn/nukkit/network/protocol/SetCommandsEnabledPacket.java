package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class SetCommandsEnabledPacket extends DataPacket {

    public boolean enabled;

    @Override
    public byte pid() {
        return ProtocolInfo.SET_COMMANDS_ENABLED_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putBoolean(this.enabled);
    }
}

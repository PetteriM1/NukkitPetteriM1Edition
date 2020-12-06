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
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putBoolean(this.enabled);
    }
}

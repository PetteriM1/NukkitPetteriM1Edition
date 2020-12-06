package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ServerSettingsResponsePacket extends DataPacket {

    public int formId;
    public String data;

    @Override
    public byte pid() {
        return ProtocolInfo.SERVER_SETTINGS_RESPONSE_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putVarInt(this.formId);
        this.putString(this.data);
    }
}

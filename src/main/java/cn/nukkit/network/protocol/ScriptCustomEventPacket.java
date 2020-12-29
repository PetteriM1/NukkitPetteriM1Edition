package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ScriptCustomEventPacket extends DataPacket {
    
    public String eventName;
    public byte[] eventData;

    @Override
    public byte pid() {
        return ProtocolInfo.SCRIPT_CUSTOM_EVENT_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        this.eventName = this.getString();
        this.eventData = this.getByteArray();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putString(this.eventName);
        this.putByteArray(this.eventData);
    }
}

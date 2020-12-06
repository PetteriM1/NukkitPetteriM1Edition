package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class NetworkStackLatencyPacket extends DataPacket {

    public long timestamp;
    public boolean unknownBool;

    @Override
    public byte pid() {
        return ProtocolInfo.NETWORK_STACK_LATENCY_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        timestamp = this.getLLong();
    }

    @Override
    public void encode(int protocolId) {
        this.putLLong(timestamp);
        if (protocolId >= 332) {
            this.putBoolean(unknownBool);
        }
    }
}

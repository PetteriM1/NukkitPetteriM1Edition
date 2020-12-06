package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class TransferPacket extends DataPacket {

    public String address;
    public int port = 19132;

    @Override
    public void decode(int protocolId) {
        this.address = this.getString();
        this.port = (short) this.getLShort();
    }

    @Override
    public void encode(int protocolId) {
        this.putString(address);
        this.putLShort(port);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.TRANSFER_PACKET;
    }
}

package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class TransferPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.TRANSFER_PACKET;

    public String address;
    public int port = 19132;

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(address);
        this.putLShort(port);
        if (protocol >= ProtocolInfo.v1_21_30) {
            this.putBoolean(false); // reloadWorld
            if (protocol >= ProtocolInfo.v1_26_40) {
                this.putBoolean(false); // has gatheringsConfig
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

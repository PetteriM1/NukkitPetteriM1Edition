package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class CameraPacket extends DataPacket {

    public long cameraUniqueId;
    public long playerUniqueId;

    @Override
    public byte pid() {
        return ProtocolInfo.CAMERA_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.cameraUniqueId = this.getVarLong();
        this.playerUniqueId = this.getVarLong();
    }

    @Override
    public void encode(int protocolId) {
        this.putEntityUniqueId(this.cameraUniqueId);
        this.putEntityUniqueId(this.playerUniqueId);
    }
}

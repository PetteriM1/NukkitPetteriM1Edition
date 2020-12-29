package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import lombok.ToString;

@ToString
public class BlockPickRequestPacket extends DataPacket {

    public int x;
    public int y;
    public int z;
    public boolean addUserData;
    public int selectedSlot;

    @Override
    public byte pid() {
        return ProtocolInfo.BLOCK_PICK_REQUEST_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        BlockVector3 v = this.getSignedBlockPosition();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.putBoolean(this.addUserData);
        this.selectedSlot = this.getByte();
    }

    @Override
    public void encodePayload(int protocolId) {
    }
}

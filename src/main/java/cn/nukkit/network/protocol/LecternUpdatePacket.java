package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import lombok.ToString;

@ToString
public class LecternUpdatePacket extends DataPacket {

    public int page;
    public BlockVector3 blockPosition;
    public boolean unknownBool;

    @Override
    public byte pid() {
        return ProtocolInfo.LECTERN_UPDATE_PACKET;
    }

    @Override
    public void decode() {
        page = this.getByte();
        blockPosition = this.getBlockVector3();
        unknownBool = this.getBoolean();
    }

    @Override
    public void encode() {
    }
}

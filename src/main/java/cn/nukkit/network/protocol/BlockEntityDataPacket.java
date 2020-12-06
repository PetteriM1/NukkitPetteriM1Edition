package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString(exclude = "namedTag")
public class BlockEntityDataPacket extends DataPacket {

    public int x;
    public int y;
    public int z;
    public byte[] namedTag;

    @Override
    public byte pid() {
        return ProtocolInfo.BLOCK_ENTITY_DATA_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        BlockVector3 v = this.getBlockVector3();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.namedTag = this.get();
    }

    @Override
    public void encode(int protocolId) {
        this.putBlockVector3(this.x, this.y, this.z);
        this.put(this.namedTag);
    }
}
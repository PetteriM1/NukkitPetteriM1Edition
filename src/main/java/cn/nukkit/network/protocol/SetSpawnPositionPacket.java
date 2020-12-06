package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class SetSpawnPositionPacket extends DataPacket {

    public static final int TYPE_PLAYER_SPAWN = 0;
    public static final int TYPE_WORLD_SPAWN = 1;

    public int spawnType;
    public int y;
    public int z;
    public int x;
    public int dimension = 0;

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putVarInt(this.spawnType);
        this.putBlockVector3(this.x, this.y, this.z);
        if (protocolId >= 407) {
            this.putVarInt(this.dimension);
            this.putBlockVector3(this.x, this.y, this.z);
        } else {
            this.putBoolean(false);
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SET_SPAWN_POSITION_PACKET;
    }
}

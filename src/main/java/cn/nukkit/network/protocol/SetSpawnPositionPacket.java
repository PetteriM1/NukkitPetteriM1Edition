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
    public boolean spawnForced = false;
    public int dimension = 0;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.spawnType);
        this.putBlockVector3(this.x, this.y, this.z);
        this.putBoolean(this.spawnForced);
        if (protocol > 407) {
            this.putVarInt(dimension);
            this.putBlockVector3(this.x, this.y, this.z); //block position: spawn
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SET_SPAWN_POSITION_PACKET;
    }
}
